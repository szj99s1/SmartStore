package com.daofu.commons.propertie;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

/**
 * @author li-chuang
 * @date created in 2018/12/17 11:27
 * @description
 */
@Data
@Order(1)
@Configuration
public class CompUtils {
    /**
     *
     */
    @Getter
    private static String endpoint;
    @Getter
    private static String bucketName;
    @Getter
    private static String accessKey;
    @Getter
    private static String accessKeySecret;
    @Getter
    private static String accessUrl;
    @Getter
    private static int freesdkPoolNum;
    @Getter
    private static boolean oss;
    @Getter
    private static String fileRootPath;
    @Getter
    private static String applicationUrl;
    @Getter
    private static String baiduAppId;
    @Getter
    private static String baiduAppKey;
    @Getter
    private static String baiduSecretKey;
    @Getter
    private static int daofuFaceCheckType;
    @Getter
    private static int duplicateLockSecond;
    @Getter
    private static String serviceUrlSms;
    @Getter
    private static int threadPoolTask1Num;
    @Getter
    private static int threadPoolTask2Num;
    @Getter
    private static int featurePartitionSize;

    @Value("${file.oss.endpoint}")
    public String pendpoint;
    @Value("${file.oss.bucketName}")
    public String pbucketName;
    @Value("${file.oss.accessKey}")
    public String paccessKey;
    @Value("${file.oss.accessKeySecret}")
    public String paccessKeySecret;
    @Value("${file.oss.accessUrl}")
    public String paccessUrl;
    @Value("${config.freesdk.pool-size}")
    public int pfreesdkPoolNum;
    @Value("${file.oss.open}")
    public boolean poss;
    @Value("${file.root.path}")
    public String pfileRootPath;
    @Value("${application.url}")
    public String papplicationUrl;
    @Value("${baidu.appId}")
    public String pbaiduAppId;
    @Value("${baidu.apiKey}")
    public String pbaiduAppKey;
    @Value("${baidu.secretKey}")
    public String pbaiduSecretKey;
    @Value("${daofu.faceCheck.type}")
    public int pdaofuFaceCheckType;
    @Value("${duplicate.lock.second}")
    public int pduplicateLockSecond;
    @Value("${service.url.sms}")
    public String pserviceUrlSms;
    @Value("${thread.pool.task1.num}")
    public int pthreadPoolTask1Num;
    @Value("${thread.pool.task2.num}")
    public int pthreadPoolTask2Num;
    @Value("${feature.partition.size}")
    public int pfeaturePartitionSize;

    public @PostConstruct void getComp() {
        endpoint=pendpoint;
        bucketName=pbucketName;
        accessKey=paccessKey;
        accessKeySecret=paccessKeySecret;
        accessUrl=paccessUrl;
        freesdkPoolNum=pfreesdkPoolNum;
        oss=poss;
        fileRootPath=pfileRootPath;
        applicationUrl=papplicationUrl;
        baiduAppId=pbaiduAppId;
        baiduAppKey=pbaiduAppKey;
        baiduSecretKey=pbaiduSecretKey;
        daofuFaceCheckType=pdaofuFaceCheckType;
        duplicateLockSecond=pduplicateLockSecond;
        serviceUrlSms=pserviceUrlSms;
        threadPoolTask1Num= pthreadPoolTask1Num;
        threadPoolTask2Num= pthreadPoolTask2Num;
        featurePartitionSize = this.pfeaturePartitionSize;
    }
}
