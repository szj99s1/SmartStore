package com.daofu.faceserver.model;

import java.io.Serializable;

/**
 * @author
 */
public class BlacklistLog implements Serializable{

  private static final long serialVersionUID = 1441910020094468096L;
  /**
   * 创建时间
   */
  private String createTime;
  /**
   * 店铺id
   */
  private Integer shopId;
  /**
   * 设备编码
   */
  private String deviceCode;
  /**
   * 图片URL
   */
  private String faceUrl;
  /**
   * 底裤地址
   */
  private String originalUrl;
  /**
   * 识别记录ID
   */
  private String recId;
  /**
   * 真实姓名
   */
  private String realName;
  /**
   * 机构id
   */
  private Integer subOrgId;
  /**
   * 性别 0男 1女 
   */
  private Integer sex;
  /**
   * 黑名单ID
   */
  private String userId;

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public Integer getShopId() {
    return shopId;
  }

  public void setShopId(Integer shopId) {
    this.shopId = shopId;
  }

  public String getDeviceCode() {
    return deviceCode;
  }

  public void setDeviceCode(String deviceCode) {
    this.deviceCode = deviceCode;
  }

  public String getFaceUrl() {
    return faceUrl;
  }

  public void setFaceUrl(String faceUrl) {
    this.faceUrl = faceUrl;
  }

  public String getOriginalUrl() {
    return originalUrl;
  }

  public void setOriginalUrl(String originalUrl) {
    this.originalUrl = originalUrl;
  }

  public String getRecId() {
    return recId;
  }

  public void setRecId(String recId) {
    this.recId = recId;
  }

  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  public Integer getSubOrgId() {
    return subOrgId;
  }

  public void setSubOrgId(Integer subOrgId) {
    this.subOrgId = subOrgId;
  }

  public Integer getSex() {
    return sex;
  }

  public void setSex(Integer sex) {
    this.sex = sex;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
