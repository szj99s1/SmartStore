package com.daofu.faceserver.model;

import lombok.Data;

/**
 * @ClassName FaceDetailInfo
 * @Author shenzj
 * @Date 2019/1/22 17:54
 **/
@Data
public class FaceDetailInfo {
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别（0：男，1：女）
     */
    private Integer gender;
    /**
     * 左右角度
     */
    private Float yaw;
    /**
     * 上下角度
     */
    private Float pitch;
    /**
     * 顺时针旋转角度
     */
    private Float roll;
    /**
     * 头像偏左距离
     */
    private Integer left;
    /**
     * 头像偏上距离
     */
    private Integer top;
    /**
     * 头像宽度
     */
    private Integer width;
    /**
     * 头像高度
     */
    private Integer height;
    /**
     *
     */
    private Integer orient;
    /**
     *
     */
    private Integer status;
}
