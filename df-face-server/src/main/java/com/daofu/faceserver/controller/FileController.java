package com.daofu.faceserver.controller;

import com.daofu.commons.utils.JsonResultBuilder;
import com.daofu.faceserver.task.FileTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author lichuang
 */
@RestController
@RequestMapping("/api")
public class FileController {
    private static Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileTask fileTask;

    /**
     * @description 刷脸日志上报
     * @author lc
     * @date 2019-02-11 17:16
     * @param deviceId
     * @param file
     * @return com.alibaba.fastjson.JSONObject
     */
    @PostMapping(value = "devicefaces/upload")
    public Map<String, Object> upload(@RequestParam("device_id") String deviceId, @RequestParam(value = "files", required = false) MultipartFile file){
        if(file == null || file.isEmpty()){
            logger.error("上传文件为空");
            return JsonResultBuilder.fail().toMap();
        }
        logger.info("刷脸日志上传：device_id:" + deviceId + ", 文件名称：" + file.getOriginalFilename());
        fileTask.uploadLog(deviceId, file);
        return JsonResultBuilder.success().toMap();
    }

}
