package com.daofu.faceserver.model;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class VipUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;                   
	private String realName;
	private String vipNo;
	private Integer orgId ;
	private Integer shopId ;
	private Integer vipTypeId;
	private String mobile  ;                 
	private Integer sex ;                           
	private String faceImgUrl;
	private String birthday  ;                 
	private Integer employeeId  ;
	private String thirdNumber ;
	private String gmtJoin   ;
	private String wechat  ;
	private String specificMemo ;
	private String memo     ; 
	private Integer status   ; 
	private String gmtCreate  ;
	private String gmtModify   ;
	private String userLabels ;
	private Integer createUserId ;
	private Integer subOrgId  ;
	private String vipTypeName ;
	private String shopName;
	private String eigenvalue;
	private int openType;
	private int blacklistFlag;
	private Date visittime;
}
