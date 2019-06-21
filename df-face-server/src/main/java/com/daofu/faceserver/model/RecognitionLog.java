package com.daofu.faceserver.model;

import lombok.Data;

@Data
public class RecognitionLog {
	private Long id;
	private String create_time;
	private int org_id ;
	private int shop_id;
	private String device_code;
	private String face_url;
	private String original_url;
	private String obj_key;
	private int user_id;
	private Integer score;
	private String name;
	private String unit_name;
	private int user_type;
	private int sub_org_id;
	private int entrance;
	private String rec_id;
	private String  equipment_position;
	private String  equipment_name;
	private Integer sex;
	private Integer age;
	private Float angel_yaw;
	private Float angel_pitch;
	private String user_name;
}
