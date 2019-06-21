package com.daofu.faceserver.model;

import lombok.Data;

/**
 * @author lichuang
 * @description
 * @date 2019-05-06 16:30
 */
@Data
public class FaceUserInfo {
    private Integer score;
    private String feature;
    private byte[] faceFeature;

    public FaceUserInfo(){}

    public FaceUserInfo(String feature, byte[] faceFeature){
        this.feature = feature;
        this.faceFeature = faceFeature;
    }
}
