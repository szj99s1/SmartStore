package com.daofu.faceserver.service;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectResultOrient;
import com.arcsoft.face.enums.ImageFormat;
import com.daofu.commons.propertie.CompUtils;
import com.daofu.faceserver.base.ImageInfo;
import com.daofu.faceserver.factory.FaceEngineFactory;
import com.daofu.faceserver.model.*;
import com.google.common.collect.Lists;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName FaceService
 * @Author shenzj
 * @Date 2019/1/22 16:20
 **/
@Service
public class FaceService {
    public final static Logger logger = LoggerFactory.getLogger(FaceService.class);
    @Value("${config.freesdk.app-id}")
    public String appId;
    @Value("${config.freesdk.sdk-key}")
    public String sdkKey;
    @Value("${config.freesdk.pool-size}")
    public int poolSize;
    @Value("${thread.pool.task2.num}")
    public int task2PoolSize;
    private ExecutorService executorService;

    private GenericObjectPool<FaceEngine> faceEngineObjectPool;
    private GenericObjectPool<FaceEngine> compareFaceObjectPool;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(task2PoolSize);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(poolSize);
        poolConfig.setMaxTotal(poolSize);
        poolConfig.setMinIdle(poolSize);
        poolConfig.setLifo(false);
        //底层库算法对象池
        faceEngineObjectPool = new GenericObjectPool(new FaceEngineFactory(appId, sdkKey, FunctionConfiguration.builder().supportFaceDetect(true).supportFaceRecognition(true).supportAge(true).supportGender(true).supportFace3dAngle(true).build()), poolConfig);
        compareFaceObjectPool = new GenericObjectPool(new FaceEngineFactory(appId, sdkKey, FunctionConfiguration.builder().supportFaceRecognition(true).build()), poolConfig);
    }

    /**
     *功能描述 人脸检测
     * @author shenzj
     * @date 2019/1/22
     */
    public List<FaceDetailInfo> detectFaces(ImageInfo imageInfo) {
        List<FaceDetailInfo> faceDetailInfoList = new ArrayList<>();
        FaceEngine faceEngine = null;
        try{
            List<FaceInfo> faceInfoList = new ArrayList<>();
            faceEngine = faceEngineObjectPool.borrowObject();
            faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
            if(faceInfoList.size()>0){
                int processResult = faceEngine.process(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList, FunctionConfiguration.builder().supportAge(true).supportFace3dAngle(true).supportGender(true).build());
                //性别提取
                List<GenderInfo> genderInfoList = new ArrayList<>();
                int genderCode = faceEngine.getGender(genderInfoList);

                //年龄提取
                List<AgeInfo> ageInfoList = new ArrayList<>();
                int ageCode = faceEngine.getAge(ageInfoList);

                //3D信息提取
                List<Face3DAngle> face3DAngleList = new ArrayList<>();
                int face3dCode = faceEngine.getFace3DAngle(face3DAngleList);

                for(int i=0;i<faceInfoList.size();i++){
                    FaceDetailInfo faceDetailInfo = new FaceDetailInfo();
                    faceDetailInfo.setAge(ageInfoList.get(i).getAge());
                    faceDetailInfo.setGender(genderInfoList.get(i).getGender());
                    faceDetailInfo.setYaw(face3DAngleList.get(i).getYaw());
                    faceDetailInfo.setPitch(face3DAngleList.get(i).getPitch());
                    faceDetailInfo.setRoll(face3DAngleList.get(i).getRoll());
                    faceDetailInfo.setStatus(face3DAngleList.get(i).getStatus());
                    faceDetailInfo.setOrient(faceInfoList.get(i).getOrient());
                    Rect rect = faceInfoList.get(i).getRect();
                    faceDetailInfo.setLeft(rect.getLeft());
                    faceDetailInfo.setTop(rect.getTop());
                    faceDetailInfo.setWidth(rect.getRight() - rect.getLeft());
                    faceDetailInfo.setHeight(rect.getBottom() - rect.getTop());
                    faceDetailInfoList.add(faceDetailInfo);
                }
            }
        }catch (Exception e){
            logger.error("detectFaces异常", e);
        }finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }
        }
        return faceDetailInfoList;
    }

    /**
     *功能描述  通过人脸图片比对
     * @author shenzj
     * @date 2019/1/23
     */
    public CompareResult compareFacesByImages(ImageInfo imageInfo0, ImageInfo imageInfo1){
        CompareResult result =   new CompareResult();
        FaceEngine  faceEngine = null;
        try{
            faceEngine = faceEngineObjectPool.borrowObject();
            //人脸检测
            List<FaceInfo> faceInfoList0 = new ArrayList<>();
            faceEngine.detectFaces(imageInfo0.getRgbData(), imageInfo0.getWidth(), imageInfo0.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList0);
            //提取人脸特征
            FaceFeature faceFeature0 = new FaceFeature();
            faceEngine.extractFaceFeature(imageInfo0.getRgbData(), imageInfo0.getWidth(), imageInfo0.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList0.get(0), faceFeature0);

            //人脸检测
            List<FaceInfo> faceInfoList1 = new ArrayList<>();
            faceEngine.detectFaces(imageInfo1.getRgbData(), imageInfo1.getWidth(), imageInfo1.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList1);
            //提取人脸特征
            FaceFeature faceFeature1 = new FaceFeature();
            faceEngine.extractFaceFeature(imageInfo1.getRgbData(), imageInfo1.getWidth(), imageInfo1.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList1.get(0), faceFeature1);

            FaceSimilar faceSimilar = new FaceSimilar();
            faceEngine.compareFaceFeature(faceFeature0, faceFeature1, faceSimilar);
            float score = faceSimilar.getScore();
            result.setScore(score);
        }catch(Exception e){
            logger.error("compareFacesByImages异常", e);
        }finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }
        }
        return result;
    }

    /**
     *功能描述  通过人脸特征值比对
     * @author shenzj
     * @date 2019/1/23
     */
    public CompareResult compareFacesByFeatures(byte[] feature0,byte[] feature1){
        CompareResult result = new CompareResult();
        FaceEngine faceEngine = null;
        try{
            faceEngine = faceEngineObjectPool.borrowObject();
            FaceSimilar faceSimilar = new FaceSimilar();
            FaceFeature faceFeature0 = new FaceFeature();
            faceFeature0.setFeatureData(feature0);
            FaceFeature faceFeature1 = new FaceFeature();
            faceFeature1.setFeatureData(feature1);
            faceEngine.compareFaceFeature(faceFeature0, faceFeature1, faceSimilar);
            float score = faceSimilar.getScore();
            result.setScore(score);
        }catch(Exception e){
            logger.error("compareFacesByFeatures异常", e);
        }finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }
        }
        return result;
    }

    /**
     *功能描述 获取人脸图像的特征值
     * @author shenzj
     * @date 2019/1/31
     */
    public FeatureData getFeatureByImage(ImageInfo imageInfo){
        FeatureData featureData = null;
        FaceEngine faceEngine = null;
        try{
            faceEngine = faceEngineObjectPool.borrowObject();
            //人脸检测
            List<FaceInfo> faceInfoList = new ArrayList<>();
            faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
            if(faceInfoList.isEmpty()){
                return null;
            }
            featureData = new FeatureData();
            faceEngine.process(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList, FunctionConfiguration.builder().supportAge(true).supportFace3dAngle(true).supportGender(true).build());
            List<AgeInfo> ageInfoList = new ArrayList<>();
            faceEngine.getAge(ageInfoList);
            List<GenderInfo> genderInfoList = new ArrayList<>();
            faceEngine.getGender(genderInfoList);
            List<Face3DAngle> face3DAngleList = new ArrayList<>();
            faceEngine.getFace3DAngle(face3DAngleList);
            featureData.setAge(ageInfoList.get(0).getAge());
            featureData.setGender(genderInfoList.get(0).getGender());
            Face3DAngle face3DAngle = face3DAngleList.get(0);
            featureData.setPitch(face3DAngle.getPitch());
            featureData.setRoll(face3DAngle.getRoll());
            featureData.setYaw(face3DAngle.getYaw());
            Rect rect = faceInfoList.get(0).getRect();
            featureData.setLeft(rect.getLeft());
            featureData.setTop(rect.getTop());
            featureData.setWidth(rect.getRight() - rect.getLeft());
            featureData.setHeight(rect.getBottom() - rect.getTop());
            //提取人脸特征
            FaceFeature faceFeature = new FaceFeature();
            faceEngine.extractFaceFeature(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
            featureData.setFeatureData(Base64Utils.encodeToString(faceFeature.getFeatureData()));
        }catch(Exception e){
            logger.error("getFeatureByImage异常", e);
        }finally {
            if (faceEngine != null) {
                //释放引擎对象
                faceEngineObjectPool.returnObject(faceEngine);
            }
        }
        return featureData;
    }

    public FaceData compareFaceFeature(byte[] faceFeature, List<FaceUserInfo> faceInfoList, int compareScore) throws InterruptedException, ExecutionException {
        List<FaceUserInfo> resultFaceInfoList = Lists.newLinkedList();

        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature);

        List<List<FaceUserInfo>> faceUserInfoPartList = Lists.partition(faceInfoList, CompUtils.getFeaturePartitionSize());
        CompletionService<List<FaceUserInfo>> completionService = new ExecutorCompletionService(executorService);
        for (List<FaceUserInfo> part : faceUserInfoPartList) {
            completionService.submit(new CompareFaceTask(part, targetFaceFeature, compareScore));
        }
        for (int i = 0; i < faceUserInfoPartList.size(); i++) {
            List<FaceUserInfo> faceUserInfoList = completionService.take().get();
            if (!faceInfoList.isEmpty()) {
                resultFaceInfoList.addAll(faceUserInfoList);
            }
        }
        resultFaceInfoList.sort((h1, h2) -> h2.getScore().compareTo(h1.getScore()));
        if(resultFaceInfoList.isEmpty()){
            return null;
        }
        return new FaceData()
                .setScore(resultFaceInfoList.get(0).getScore())
                .setFeature(resultFaceInfoList.get(0).getFeature());
    }

    private class CompareFaceTask implements Callable<List<FaceUserInfo>> {

        private List<FaceUserInfo> faceUserInfoList;
        private FaceFeature targetFaceFeature;
        private int compareScore;

        public CompareFaceTask(List<FaceUserInfo> faceUserInfoList, FaceFeature targetFaceFeature, int compareScore) {
            this.faceUserInfoList = faceUserInfoList;
            this.targetFaceFeature = targetFaceFeature;
            this.compareScore = compareScore;
        }

        @Override
        public List<FaceUserInfo> call() throws Exception {
            FaceEngine faceEngine = null;
            //识别到的人脸列表
            List<FaceUserInfo> resultFaceInfoList = Lists.newLinkedList();
            try {
                faceEngine = compareFaceObjectPool.borrowObject();
                for (FaceUserInfo faceUserInfo : faceUserInfoList) {
                    FaceFeature sourceFaceFeature = new FaceFeature();
                    sourceFaceFeature.setFeatureData(faceUserInfo.getFaceFeature());
                    FaceSimilar faceSimilar = new FaceSimilar();
                    faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
                    //获取相似值
                    Integer score = plusHundred(faceSimilar.getScore());
                    if (score >= compareScore) {
                        FaceUserInfo info = new FaceUserInfo();
                        info.setScore(score);
                        info.setFeature(faceUserInfo.getFeature());
                        resultFaceInfoList.add(info);
                    }
                }
            } catch (Exception e) {
                logger.error("批量比对失败：", e);
            } finally {
                if (faceEngine != null) {
                    compareFaceObjectPool.returnObject(faceEngine);
                }
            }
            return resultFaceInfoList;
        }
    }

    private int plusHundred(Float value) {
        BigDecimal target = new BigDecimal(value);
        BigDecimal hundred = new BigDecimal(100f);
        return target.multiply(hundred).intValue();
    }

}
