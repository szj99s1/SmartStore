package com.daofu.commons.utils.file;

import com.daofu.commons.propertie.CompUtils;

import java.io.IOException;

/**
 * oss相关参数实体
 * @author liux
 *2017/5/5
 */
public class OSSConfigure {

  private String endpoint;
  private String accessKeyId;
  private String accessKeySecret;
  private String bucketName;
  private String accessUrl;

  /**
   * 通过配置文件.properties文件获取，这几项内容。
   *
   * @throws IOException
   */
  public OSSConfigure(){

      endpoint = CompUtils.getEndpoint();
      accessKeyId = CompUtils.getAccessKey();
      accessKeySecret = CompUtils.getAccessKeySecret();
      bucketName = CompUtils.getBucketName();
      accessUrl = CompUtils.getAccessUrl();

  }

  public OSSConfigure(String endpoint, String accessKeyId,
          String accessKeySecret, String bucketName, String accessUrl) {

      this.endpoint = endpoint;
      this.accessKeyId = accessKeyId;
      this.accessKeySecret = accessKeySecret;
      this.bucketName = bucketName;
      this.accessUrl = accessUrl;
  }

  public String getEndpoint() {
      return endpoint;
  }

  public void setEndpoint(String endpoint) {
      this.endpoint = endpoint;
  }

  public String getAccessKeyId() {
      return accessKeyId;
  }

  public void setAccessKeyId(String accessKeyId) {
      this.accessKeyId = accessKeyId;
  }

  public String getAccessKeySecret() {
      return accessKeySecret;
  }

  public void setAccessKeySecret(String accessKeySecret) {
      this.accessKeySecret = accessKeySecret;
  }

  public String getBucketName() {
      return bucketName;
  }

  public void setBucketName(String bucketName) {
      this.bucketName = bucketName;
  }

  public String getAccessUrl() {
      return accessUrl;
  }

  public void setAccessUrl(String accessUrl) {
      this.accessUrl = accessUrl;
  }

}