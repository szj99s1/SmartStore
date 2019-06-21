package com.daofu.faceserver.dao;

import com.daofu.faceserver.model.Organization;
import com.daofu.faceserver.model.Shop;
import com.daofu.faceserver.model.Stranger;
import com.daofu.faceserver.model.VipUser;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author lichuang
 * @description
 * @date 2019-02-13 13:26
 */
public interface FileDao {
    /**
     * @description 插入陌生人
     * @author lc
     * @date 2019-02-13 13:33
     * @param stranger
     * @return int
     */
    int addStranger(Stranger stranger);
    /**
     * @description 查询最近几天内到访的陌生人
     * @author lc
     * @date 2019-02-13 13:50
     * @param visittime
     * @param shopId
     * @return java.util.List<com.daofu.faceserver.model.Stranger>
     */
    List<Stranger> selectStrangerLastTime(@Param("visittime") String visittime, @Param("shopId") Integer shopId, @Param("sex") Integer sex);
    /**
     * @description 更新最近到访时间
     * @author lc
     * @date 2019-02-13 14:26
     * @param
     * @return int
     */
    int updateStrangerVisittime(@Param("userId")String userId, @Param("date")Date date);
    /**
     * @description
     * @author lc
     * @date 2019-02-27 13:45
     * @param feature
     * @return com.daofu.faceserver.model.Stranger
     */
    Stranger findUserByFeature(String feature, Date date);
    /**
     * @description 通过机构id找到所有会员
     * @author lc
     * @date 2019-03-18 16:39
     * @param subOrgId
     * @return java.util.List<com.daofu.faceserver.model.VipUser>
     */
    List<Integer> selectShopIdListBySubOrgId(@Param("subOrgId") int subOrgId, @Param("shopId") int shopId);
    /**
     * @description 根据shopId查询回头客天数
     * @author lc
     * @date 2019-03-21 11:19
     * @param shopId
     * @return com.daofu.faceserver.model.Organization
     */
    Organization selectSubOrgInfoByShopId(int shopId);
}
