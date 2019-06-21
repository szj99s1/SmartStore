package com.daofu.commons.bean;

import com.daofu.commons.redis.RedisClient;
import com.daofu.faceserver.controller.FaceController;
import com.daofu.faceserver.service.FileService;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author lichuang
 * @description
 * @date 2019-02-18 17:40
 */
@Configuration
public class BeanUtils {

    private static RedisClient redisClient;
    private static FileService fileService;
    private static FaceController faceController;

    @Resource(name="redisClient")
    private void setRedisTemplate(RedisClient redisClient) {
        BeanUtils.redisClient = redisClient;
    }
    @Resource(name="fileService")
    private void setFileService(FileService fileService) {
        BeanUtils.fileService = fileService;
    }
    @Resource(name="faceController")
    private void setFileService(FaceController faceController) {
        BeanUtils.faceController = faceController;
    }

    public static RedisClient getRedisClient(){
        return redisClient;
    }
    public static FileService getFileService(){
        return fileService;
    }
    public static FaceController getFaceController(){
        return faceController;
    }
}
