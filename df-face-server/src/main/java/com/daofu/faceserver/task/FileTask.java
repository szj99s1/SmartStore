package com.daofu.faceserver.task;

import com.daofu.commons.bean.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lichuang
 * @description
 * @date 2019-02-28 09:31
 */
@Component
public class FileTask {

    private static final Logger logger = LoggerFactory.getLogger(FileTask.class);

    @Async("tank1Executor")
    public void uploadLog(String deviceId, MultipartFile file) {
        long start = System.currentTimeMillis();
        logger.info("日志文件上传处理开始！");
        BeanUtils.getFileService().uploadLog(deviceId, file);
        logger.info("日志文件上传处理成功！耗时：" + (System.currentTimeMillis() - start) + "毫秒");
    }
}
