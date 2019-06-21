//package com.daofu.faceserver.redis;
//
//import com.daofu.commons.redis.RedisClient;
//import com.daofu.commons.utils.GsonUtils;
//import com.daofu.faceserver.model.Equipment;
//import com.daofu.faceserver.service.FileService;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @author lichuang
// * @description
// * @date 2019-02-11 17:27
// */
//@Component
//public class FileRedis {
//
//    @Autowired
//    private RedisClient redisClient;
//
//    @Autowired
//    private FileService fileService;
//
//    private static final String DEVICE_PRE = "DEVICE:";
//
//    /**
//     * @description 获取设备信息
//     * @author lc
//     * @date 2019-02-28 10:41
//     * @param deviceId
//     * @return com.daofu.faceserver.model.Equipment
//     */
//    public Equipment getEquipment(String deviceId){
//        String equipmentJsonStr = redisClient.get(DEVICE_PRE + deviceId);
//        if(!StringUtils.isBlank(equipmentJsonStr)){
//            return GsonUtils.fromJson(equipmentJsonStr, Equipment.class);
//        }
//        Equipment equipment = fileService.getByDeviceCode(deviceId);
//        if (equipment != null) {
//            redisClient.setex(DEVICE_PRE + deviceId, 43200, GsonUtils.toJson(equipment));
//            return equipment;
//        }
//        return null;
//    }
//}
