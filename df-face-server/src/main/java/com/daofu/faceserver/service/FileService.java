package com.daofu.faceserver.service;

import com.daofu.commons.bean.BeanUtils;
import com.daofu.commons.constant.CommonConstant;
import com.daofu.commons.propertie.CompUtils;
import com.daofu.commons.redis.RedisClient;
import com.daofu.commons.utils.*;
import com.daofu.commons.utils.file.FileUtil;
import com.daofu.faceserver.constant.FaceServerConstant;
import com.daofu.faceserver.controller.FaceController;
import com.daofu.faceserver.dao.*;
import com.daofu.faceserver.enums.PeopleTypeEnum;
import com.daofu.faceserver.model.*;
import com.daofu.faceserver.task.FileTask;
import com.daofu.faceserver.util.FaceCheckUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lichuang
 */
@Service
public class FileService {
	private static Logger logger = LoggerFactory.getLogger(FileService.class);

	@Autowired
	private RedisClient redisClient;
	@Autowired
	private FaceController faceController;

	@Resource
	RecognitionLogDao recognitionLogDao;

	@Resource
	StrangerLogDao strangerLogDao;

	@Resource
	FileDao fileDao;

	@Resource
	BlacklistLogDao blacklistLogDao;

	@Resource
	EquipmentDao equipmentDao;
	
	@Resource
	VipUserDao vipUserDao;

	@Autowired
	private FileTask fileTask;

	public int addRecognitionLog(RecognitionLog log){
		return recognitionLogDao.add(log);
	}

	public int addStrangerLog(StrangerLog log){
		return strangerLogDao.add(log);
	}

	public int addStranger(Stranger stranger){
		return fileDao.addStranger(stranger);
	}

	public int updateStrangerVisittime(String userId, Date date){
		return fileDao.updateStrangerVisittime(userId, date);
	}

	public Stranger findStrangerByFeature(String feature, Date date){
		return fileDao.findUserByFeature(feature, date);
	}

	public Equipment getByDeviceCode(String device_code){
		return equipmentDao.getByDeviceCode(device_code);
	}
	
	public VipUser getVipUserById(int  id){
		return vipUserDao.getVipUserById(id);
	}

	public int updateVisitTimeById(int id, String timestamp){
		return vipUserDao.updateVisitTimeById(id, timestamp);
	}

	public Blacklist getBlacklist(Integer vipId, Integer blacklistId){
		return vipUserDao.getBlacklist(vipId, blacklistId);
	}

	public int updateBlackListVisitTimeById(int id){
		return vipUserDao.updateBlackListVisitTimeById(id);
	}

	public int addBlacklistLog(BlacklistLog bdto) {
		return blacklistLogDao.add(bdto);
	}

	/**
	 * @description 校验是否在一定时间内出现过
	 * @author lc
	 * @date 2019-02-18 09:33
	 * @param equipment
	 * @param featureNow
	 * @return boolean
	 */
	public boolean isComeNew(Equipment equipment, String featureNow){
		int shopId = equipment.getShopId();
		final String beforeDataKey = "user:come:before:data:" + shopId;
		final String shopFilterLibKey = "user:come:before:lib:" + shopId;
		// 设备锁定处理
		long nowMillis = System.currentTimeMillis();
		long preMillis = nowMillis - equipment.getUserFilterTime() * FaceServerConstant.SECOND_MILLIS;
        String field = nowMillis + "-" + DataUtils.getRandam();
		// 获取一定时间内出现过的特征值list
		Set<String> filedSet = redisClient.hkeys(beforeDataKey);
		// 获取所有店铺过滤图片
		Set<String> shopFilterSet = redisClient.smembers(shopFilterLibKey);
		List<String> filedList = filedSet.stream().filter(x -> preMillis < Long.parseLong(x.split("-")[0])).collect(Collectors.toList());

		// 如果需要过滤的集合为空，将当前特征值设置进去并返回
		if(filedList.isEmpty() && shopFilterSet.isEmpty()){
			BeanUtils.getRedisClient().hset(beforeDataKey, field, featureNow);
			return true;
		}

		List<String> featureList = Lists.newArrayList();
		if(!filedList.isEmpty()){
			featureList = BeanUtils.getRedisClient().hmget(beforeDataKey, filedList.toArray(new String[]{}));
		}

		// 获取比对信息
		List<String> allFilterList = Lists.newArrayList(shopFilterSet);
		allFilterList.addAll(featureList);
		FaceData faceData = faceController.compareFaceByFeatureList(new FeatureBatchCond(equipment.getCompareScore(), featureNow, allFilterList));
		// 删除超时的特征值
		List<String> delList = filedSet.stream().filter(x -> preMillis > Long.parseLong(x.split("-")[0])).collect(Collectors.toList());
		if(!delList.isEmpty()){
			BeanUtils.getRedisClient().hdel(beforeDataKey, delList.toArray(new String[]{}));
		}
		if(faceData != null){
			logger.info("比对结果：{}", faceData);
			return false;
		}
		BeanUtils.getRedisClient().hset(beforeDataKey, field, featureNow);
		return true;
	}

	/**
	 * @description
	 * @author lc
	 * @date 2019-02-18 09:33
	 * @param equipment
	 * @param featureData
	 * @return com.daofu.faceserver.model.FaceData
	 */
	public FaceData getFaceData(Equipment equipment, String featureData, int sex){
		int shopId = equipment.getShopId();
		// 依次从redis取出会员数据比对，员工-黑名单-本店会员(男或女)-机构其他门店会员(男或女)-陌生人记录（男或女），分值超过配置的分数立即返回不继续对比
		final String staffKey = this.getKey(shopId, PeopleTypeEnum.STAFF.ordinal(), 0);
		Set<String> featureList = redisClient.hkeys(staffKey);
		FaceData faceData = this.getFaceData(equipment, staffKey, featureData, featureList, PeopleTypeEnum.STAFF.ordinal());
		if(faceData != null){
			return faceData;
		}

		final String blackListKey = this.getKey(shopId, PeopleTypeEnum.BLACKLIST.ordinal(), 0);
		featureList = redisClient.hkeys(blackListKey);
		faceData = this.getFaceData(equipment, blackListKey, featureData, featureList, PeopleTypeEnum.BLACKLIST.ordinal());

		if(faceData != null){
			return faceData;
		}

		final String vipUserKey = this.getKey(shopId, PeopleTypeEnum.VIPUSER.ordinal(), sex);
		featureList = redisClient.hkeys(vipUserKey);
		faceData = this.getFaceData(equipment, vipUserKey, featureData, featureList, PeopleTypeEnum.VIPUSER.ordinal());

		if(faceData != null){
			return faceData;
		}

		// 查询该机构下所有门店id，并通过门店id找出该机构的所有会员比对
		List<Integer> shopIdList = fileDao.selectShopIdListBySubOrgId(equipment.getSubOrgId(), shopId);
		faceData = this.getFaceData(equipment, shopIdList, featureData, PeopleTypeEnum.VIPUSER.ordinal(), sex);
		if(faceData != null){
			return faceData;
		}

		final String strangerKey = this.getKey(shopId, PeopleTypeEnum.STRANGER.ordinal(), sex);
        Set<String> millis = redisClient.hkeys(strangerKey);
        Organization organization = fileDao.selectSubOrgInfoByShopId(shopId);
		faceData = this.getFaceData(equipment, strangerKey, featureData, PeopleTypeEnum.STRANGER.ordinal(), millis, organization);
		if(faceData != null){
			return faceData;
		}

		return new FaceData(100, PeopleTypeEnum.STRANGER.ordinal(), null, null, organization.getReturnerDay() == null ? 0 : organization.getReturnerDay());
	}

	private FaceData getFaceData(Equipment equipment, String key, String featureNow, int type, Set<String> millis, Organization organization){
        if(millis.isEmpty()) {
            return null;
        }

        List<String> effectList = millis.stream().filter(x -> (System.currentTimeMillis() - Long.parseLong(x.split("-")[0])) <= (organization.getReturnerDay() * FaceServerConstant.STRANGER_OVER_MILLIS)).collect(Collectors.toList());
        if(organization.getReturnerDay() == null || effectList.isEmpty()){
            return null;
        }
        List<String> featureList = BeanUtils.getRedisClient().hmget(key, effectList.toArray(new String[]{}));
        FaceData faceData = BeanUtils.getFaceController().compareFaceByFeatureList(new FeatureBatchCond(equipment.getCompareScore(), featureNow, featureList));
        if(faceData == null){
            return null;
        }
        faceData.setType(type);
        faceData.setStrangerOverDay(organization.getReturnerDay());
        return faceData;
    }

	private String getKey(int shopId, int userType, int sex){
		return new StringBuilder(FaceServerConstant.PRE_KEY)
				.append(shopId)
				.append(":")
				.append(userType)
				.append(":")
				.append(sex)
				.toString();
	}

	private FaceData getFaceData(Equipment equipment, List<Integer> shopIdList, String featureData, int type, int sex){
		if(shopIdList == null || shopIdList.isEmpty()){
			return null;
		}
		List<String> featureList = Lists.newArrayList();
		for(Integer shopId : shopIdList){
            final String vipUserKey = FaceServerConstant.PRE_KEY + shopId + ":" + PeopleTypeEnum.VIPUSER.ordinal() + ":" + sex;
            featureList.addAll(redisClient.hkeys(vipUserKey));
		}
        FaceData faceData = faceController.compareFaceByFeatureList(new FeatureBatchCond(equipment.getCompareScore(), featureData, featureList));
        if(faceData == null){
            return null;
        }
        faceData.setType(type);
        for(Integer shopId : shopIdList){
            final String vipUserKey = FaceServerConstant.PRE_KEY + shopId + ":" + PeopleTypeEnum.VIPUSER.ordinal() + ":" + sex;
            String userId = BeanUtils.getRedisClient().hget(vipUserKey, faceData.getFeature());
            if(StringUtils.isNotBlank(userId)){
            	logger.info("会员所在机构下门店为：{}", shopId);
                faceData.setUserId(userId);
                break;
            }
        }
		return faceData;
	}

	private FaceData setFaceData(Equipment equipment, Collection<String> featureList, String featureData
					, int type, String key){
		FaceData faceData = faceController.compareFaceByFeatureList(new FeatureBatchCond(equipment.getCompareScore(), featureData, featureList));
		if(faceData == null){
			return null;
		}
		faceData.setType(type);
		faceData.setUserId(BeanUtils.getRedisClient().hget(key, faceData.getFeature()));
		return faceData;
	}

	/**
	 * @description 获取人脸检测详细信息
	 * @author lc
	 * @date 2019-02-18 09:42
	 * @param key
	 * @param featureData
	 * @param featureList
	 * @param type
	 * @return com.daofu.faceserver.model.FaceData
	 */
	private FaceData getFaceData(Equipment equipment, String key, String featureData, Set<String> featureList, int type){
		if(featureList == null || featureList.isEmpty()){
			return null;
		}
		FaceData faceData = setFaceData(equipment, featureList, featureData, type, key);
		if(faceData != null){
			return faceData;
		}
		return null;
	}

	/**
	 * @description 加入陌生人表
	 * @author lc
	 * @date 2019-02-18 09:36
	 * @param equipment
	 * @param faceData
	 * @param face
	 * @param url
	 * @return void
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addStranger(SmsNotifyCond cond, Equipment equipment, FaceData faceData, String url
							, FeatureData face){
		Date date = new Date();
		String uuid;
		int returnCustomer = 0;
		if(StringUtils.isBlank(faceData.getFeature()) || faceData.getStrangerOverDay() == 0){
			uuid = DataUtils.getUuid();
			redisClient.hset(FaceServerConstant.PRE_KEY + equipment.getShopId() + ":" + PeopleTypeEnum.STRANGER.ordinal() + ":" + face.getGender(), System.currentTimeMillis() + "-" + DataUtils.getRandam(), face.getFeatureData());
			// 插入陌生人记录
			Stranger stranger = new Stranger()
					.setSex(face.getGender())
					.setFaceUrl(url)
					.setFeature(face.getFeatureData())
					.setVisittime(date)
					.setRecId(uuid)
					.setShopId(equipment.getShopId());
			this.addStranger(stranger);
		} else {
			returnCustomer = 1;
			Stranger stranger = this.findStrangerByFeature(faceData.getFeature(), DateUtils.localDateTime2Date(LocalDateTime.now().minusDays(faceData.getStrangerOverDay())));
			uuid = stranger.getRecId();
			redisClient.hset(FaceServerConstant.PRE_KEY + equipment.getShopId() + ":" + PeopleTypeEnum.STRANGER.ordinal() + ":" + face.getGender(), System.currentTimeMillis() + "-" + DataUtils.getRandam(), faceData.getFeature());
			this.updateStrangerVisittime(uuid, date);
			cond.setStamp(stranger.getVisittime());
			cond.setRemark(stranger.getMemo());
		}
		cond.setId(uuid);
		cond.setOpenType(returnCustomer == 0 ? PeopleTypeEnum.STRANGER.ordinal() : PeopleTypeEnum.RETURNER_NOTIFY.ordinal());
		this.smsSend(cond);
		this.addStrangerLog(equipment, uuid, url, faceData.getScore(), face, DateUtils.date2String(date), returnCustomer);
	}

	/**
	 * @description 添加会员或者会员黑名单日志
	 * @author lc
	 * @date 2019-02-18 09:36
	 * @param equipment
	 * @param url
	 * @param face
	 * @return void
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addVipOrVipBlacklistLog(SmsNotifyCond cond, Equipment equipment, FaceData faceData
			, String url, FeatureData face){
		String userId = faceData.getUserId();
		String timestamp = DateUtils.nowDateFormat();
		RecognitionLog dto = new RecognitionLog();
		VipUser vipUser = this.getVipUserById(Integer.parseInt(userId));
		cond.setId(userId);
		cond.setOpenType(PeopleTypeEnum.VIPUSER.ordinal());
		if (vipUser != null) {
			cond.setStamp(vipUser.getVisittime());
			dto.setOriginal_url(vipUser.getFaceImgUrl());
			dto.setUser_name(vipUser.getRealName());
			if(vipUser.getBlacklistFlag() == 1){
				this.addBlacklistVipLog(cond, userId, timestamp, equipment
						, url, vipUser, face);
			}
		}
		dto.setUser_id(Integer.parseInt(userId));
		dto.setRec_id(userId);
		dto.setShop_id(equipment.getShopId());
		dto.setOrg_id(equipment.getOrgId());
		dto.setSub_org_id(equipment.getSubOrgId());
		dto.setEquipment_position(equipment.getEquipmentPosition());
		dto.setEquipment_name(equipment.getEquipmentName());
		dto.setDevice_code(equipment.getDeviceCode());
		dto.setCreate_time(timestamp);
		dto.setFace_url(url);
		dto.setScore(faceData.getScore());

		dto.setAge(face.getAge());
		dto.setSex(face.getGender());
		dto.setAngel_yaw(face.getYaw());
		dto.setAngel_pitch(face.getPitch());

		this.smsSend(cond);

		this.addRecognitionLog(dto);
		this.updateVisitTimeById(Integer.parseInt(userId), timestamp);

	}

	/**
	 * @description 添加会员黑名单日志
	 * @author lc
	 * @date 2019-02-18 09:36
	 * @param userId
	 * @param timestamp
	 * @param equipment
	 * @param url
	 * @param vipUser
	 * @return void
	 */
	public void addBlacklistVipLog(SmsNotifyCond cond, String userId, String timestamp, Equipment equipment
			, String url, VipUser vipUser, FeatureData face){
		BlacklistLog bdto = new BlacklistLog();
		Blacklist blacklist = this.getBlacklist(Integer.parseInt(userId), null);
		if(blacklist != null){
			bdto.setUserId(String.valueOf(blacklist.getId()));
			this.updateBlackListVisitTimeById(blacklist.getId());
			cond.setId(String.valueOf(blacklist.getId()));
			cond.setStamp(blacklist.getVisittime());
			cond.setRemark(blacklist.getMemo());
		}
		cond.setOpenType(PeopleTypeEnum.VIPUSER_BLACKLIST.ordinal());

		bdto.setShopId(equipment.getShopId());
		bdto.setSubOrgId(equipment.getSubOrgId());
		bdto.setFaceUrl(url);
		bdto.setDeviceCode(equipment.getDeviceCode());
		bdto.setCreateTime(timestamp);
		bdto.setSex(face.getGender());
		bdto.setRecId(userId);
		bdto.setRealName(vipUser.getRealName());
		bdto.setOriginalUrl(vipUser.getFaceImgUrl());

		this.addBlacklistLog(bdto);
	}

	/**
	 * @description 添加黑名单日志
	 * @author lc
	 * @date 2019-02-18 09:37
	 * @param equipment
	 * @param faceData
	 * @param url
	 * @param face
	 * @return void
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addBlacklistLog(SmsNotifyCond cond, Equipment equipment, FaceData faceData, String url, FeatureData face){
		String deviceId = equipment.getDeviceCode();
		String userId = faceData.getUserId();
		String timestamp = DateUtils.nowDateFormat();
		BlacklistLog bdto = new BlacklistLog();
		Blacklist blacklist = this.getBlacklist(null, Integer.parseInt(userId));
		if(blacklist != null){
			bdto.setOriginalUrl(blacklist.getFaceImgUrl());
			bdto.setRealName(blacklist.getRealName());
			cond.setStamp(blacklist.getVisittime());
			cond.setRemark(blacklist.getMemo());
		}
		cond.setId(userId);
		cond.setOpenType(PeopleTypeEnum.BLACKLIST.ordinal());
		this.smsSend(cond);

		bdto.setShopId(equipment.getShopId());
		bdto.setSubOrgId(equipment.getSubOrgId());
		bdto.setDeviceCode(deviceId);
		bdto.setFaceUrl(url);
		bdto.setCreateTime(timestamp);
		bdto.setRecId(userId);
		bdto.setUserId(userId);
		bdto.setSex(face.getGender());

		this.addBlacklistLog(bdto);
		this.updateBlackListVisitTimeById(Integer.parseInt(userId));
		this.addStrangerLog(equipment, userId, url, faceData.getScore(), face, timestamp, 0);

	}

	/**
	 * @description 添加陌生人日志
	 * @author lc
	 * @date 2019-02-18 09:37
	 * @param equipment
	 * @param userId
	 * @param url
	 * @param score
	 * @param face
	 * @param timestamp
	 * @return void
	 */
	public void addStrangerLog(Equipment equipment, String userId, String url
			, Integer score, FeatureData face, String timestamp, int returnCustomer){
		StrangerLog dto = new StrangerLog();
		setEquipmentData(equipment, dto);
		dto.setCreateTime(timestamp);
		dto.setRecId(userId);
		dto.setFaceUrl(url);
		dto.setScore(score);
		dto.setAge(face.getAge());
		dto.setSex(face.getGender());
		dto.setAngelYaw(face.getYaw());
		dto.setAngelPitch(face.getPitch());
		dto.setReturnCustomer(returnCustomer);
		this.addStrangerLog(dto);
	}

	/**
	 * @description 设定设备信息
	 * @author lc
	 * @date 2019-02-18 09:42
	 * @param equipment
	 * @param dto
	 * @return void
	 */
	public void setEquipmentData(Equipment equipment, StrangerLog dto){
		dto.setShopId(equipment.getShopId());
		dto.setOrgId(equipment.getOrgId());
		dto.setSubOrgId(equipment.getSubOrgId());
		dto.setEquipmentPosition(equipment.getEquipmentPosition());
		dto.setEquipmentName(equipment.getEquipmentName());
		dto.setDeviceCode(equipment.getDeviceCode());
	}

	/**
	*@description消息推送
	*@authorLc
	*@date2019021809:41
	*@paramDeviceId
	*@paramStamp
	*@paramId
	*@paramOpenType
	*@returnVoid
	*/
	public void smsSend(SmsNotifyCond cond){
		cond.setSign(MD5Util.encryption("device_id=" + cond.getDeviceId() + "&stamp=" + cond.getStamp() + "&id=" + cond.getId() + "&product_id=" + cond.getProductId()));
		logger.info("消息推送返回值：" + HttpClientUtils.doPost(CompUtils.getServiceUrlSms(), GsonUtils.toJson(cond), CommonConstant.APPLICATION_JSON));
	}

	/**
	 * @description 上传日志
	 * @author lc
	 * @date 2019-02-28 09:49
	 * @param deviceId
	 * @param file
	 * @return void
	 */
	public void uploadLog(String deviceId, MultipartFile file){
		String url = null;
		try {
			// 根据设备id获取设备信息
			Equipment equipment = this.getByDeviceCode(deviceId);

			if (equipment == null) {
				logger.error("设备不存在");
				return;
			}

			if (equipment.getStatus() != 0) {
				logger.error("设备未激活或已启用");
				return;
			}

			// TODO 调接口获取特征值
//			Map<String, Object> map = new HashMap<>(2);
//			map.put("image", FaceCheckUtil.encodeBase64ByFile(file));
//			FeatureData featureData = JSONObject.parseObject(HttpClientUtils.doPost("http://192.168.6.241:9997/getFeatureByImage", GsonUtils.toJson(map), CommonConstant.APPLICATION_WWW), FeatureData.class);
			FeatureData featureData = faceController.getFeatureByImage(FaceCheckUtil.encodeBase64ByFile(file));
			if(featureData == null || StringUtils.isBlank(featureData.getFeatureData())){
				logger.error("图片特征值获取失败！");
				return;
			}

			if (equipment.getYaw() < Math.abs(featureData.getYaw())
					|| equipment.getPitch() < Math.abs(featureData.getPitch())
					|| equipment.getWidth() > featureData.getWidth()) {
				logger.error("上传图片角度不符合 -> 左右角度<" + equipment.getYaw() + "："
						+ featureData.getYaw() + ",上下角度<" + equipment.getPitch() + "：" + featureData.getPitch()
						+ ",宽度>" + equipment.getWidth() + "：" + featureData.getWidth());
				return;
			}

			// 校验是否在一定时间内已经上传过
			boolean isComeNew = this.isComeNew(equipment, featureData.getFeatureData());
			logger.info("isComeNew:{}", isComeNew);
			if(!isComeNew){
				return;
			}

			url = FileUtil.fileUpload(file, "face_log/" + DateUtils.nowDateFormatByPattern("YYYYMMdd"));
			if (url == null) {
				logger.error("图片上传失败");
				return;
			}
			logger.info("文件上传url:{}", url);

			// 与内部数据比对，校验是否存在
			FaceData faceData = this.getFaceData(equipment, featureData.getFeatureData(), featureData.getGender());
			logger.info("比对信息：{}", faceData);
			int type = PeopleTypeEnum.getType(faceData.getType());

			if(type == PeopleTypeEnum.STAFF.ordinal()){
				logger.error("员工不推送消息");
				FileUtil.delPic(url);
				return;
			}

			SmsNotifyCond cond = new SmsNotifyCond()
					.setProductId("10001")
					.setDeviceId(deviceId)
					.setImageUrl(url);

			logger.info("会员类型：{}", type);
			// 会员或会员黑名单
			if (type == PeopleTypeEnum.VIPUSER.ordinal()) {
                this.addVipOrVipBlacklistLog(cond, equipment, faceData, url, featureData);
			}
			// 黑名单
			else if (type == PeopleTypeEnum.BLACKLIST.ordinal()){
                this.addBlacklistLog(cond, equipment, faceData, url, featureData);
            }
			// 陌生人
			else if (type == PeopleTypeEnum.STRANGER.ordinal()){
                this.addStranger(cond, equipment, faceData, url, featureData);
            }
        } catch (Exception e){
			FileUtil.delPic(url);
			logger.error("日志上传处理失败:", e);
		}
	}
}
