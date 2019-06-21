package com.daofu.faceserver.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Equipment implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer orgId;
	private Integer subOrgId;
	private Integer shopId;
	private Integer equipmentType;
	private String equipmentName;
	private String equipmentPosition;
	private String remark;
	private Integer direction;
	private Integer vipRoom;
	private Integer status;
	private String deviceCode;
	private String uniqueNumber;
	private String gmtCreate;
	private String gmtModify;
	private String mac;
	private String rtspAddr;
	private Integer cameraIo;
	private Integer cameraProvider;
	private Integer devClass;
	private Integer productId;
	private String activeCode;
	private String version;
	private String activeTime;
	private String expriryTime;
	private Integer strangerSwitch;
	private Integer faceScore;
	private String ip;
	private String location;
	private Integer imageQuality;
	private String openUser;
	private String shopName;
	private String orgName;
	private String heartbeatTime;
	private int lastOnline;
	private int yaw;
	private int pitch;
	private int width;
	private int userFilterTime;
	private int compareScore;

}
