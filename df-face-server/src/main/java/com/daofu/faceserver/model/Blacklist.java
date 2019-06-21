package com.daofu.faceserver.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 */
@Data
public class Blacklist implements Serializable{

  private static final long serialVersionUID = 1475837860787260416L;
  /**
   * id
   */
  private Integer id;
  /**
   * 真实姓名
   */
  private String realName;
  private Integer shopId;
  /**
   * 人脸图片地址
   */
  private String faceImgUrl;
  private Integer subOrgId;
  /**
   * 最近到店时间
   */
  private Date visittime;
  /**
   * 备注
   */
  private String memo;
  /**
   * 性别 0男 1女 
   */
  private Integer sex;
  /**
   * 来源类型 0 会员  1陌生人识别记录
   */
  private Integer originalType;
  /**
   * 来源记录ID
   */
  private Integer originalId;
}
