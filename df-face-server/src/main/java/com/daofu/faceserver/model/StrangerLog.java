package com.daofu.faceserver.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lichuang
 */
@Data
public class StrangerLog {
	private long id;
	private int orgId;
	private int shopId;
	private String faceUrl;
	private String deviceCode;
	private String createTime;
	private Integer score;
	private int subOrgId;
	private String recId;
	private String userId;
	private String equipmentName;
	private String equipmentPosition;
	private Integer sex;
	private Integer age;
	private Float angelYaw;
	private Float angelPitch;
	private Integer returnCustomer;
}
