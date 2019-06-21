package com.daofu.faceserver.dao;

import com.daofu.faceserver.model.Blacklist;
import com.daofu.faceserver.model.VipUser;
import org.apache.ibatis.annotations.Param;


public interface VipUserDao {
 	VipUser getVipUserById(int id);
	int updateVisitTimeById(@Param("id")int id, @Param("timestamp")String timestamp);
	Blacklist getBlacklist(@Param("vipId") Integer vipId, @Param("blacklistId") Integer blacklistId);
	int updateBlackListVisitTimeById(int id);

}
