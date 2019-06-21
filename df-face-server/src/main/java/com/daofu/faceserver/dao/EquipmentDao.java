package com.daofu.faceserver.dao;


import com.daofu.faceserver.model.Equipment;

public interface EquipmentDao {

	public Equipment getByDeviceCode(String device_code);


       
}
