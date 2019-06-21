package com.daofu.faceserver.model.baidu;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @author li-chuang
 * @date created in 2019/1/4 13:23
 * @description
 */
public class FaceCheckFirst{
    /**
     * 检测到的图片中的人脸数量
     */
    @JSONField(name = "face_num")
    private Integer faceNum;
    /**
     * 人脸信息列表，具体包含的参数参考下面的列表
     */
    @JSONField(name = "face_list")
    private List<FaceDetail> faceDetail;

    public Integer getFaceNum() {
        return faceNum;
    }

    public void setFaceNum(Integer faceNum) {
        this.faceNum = faceNum;
    }

    public List<FaceDetail> getFaceDetail() {
        return faceDetail;
    }

    public void setFaceDetail(List<FaceDetail> faceDetail) {
        this.faceDetail = faceDetail;
    }
}
