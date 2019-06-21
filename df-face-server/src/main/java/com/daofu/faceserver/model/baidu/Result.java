package com.daofu.faceserver.model.baidu;

/**
 * @author li-chuang
 * @date created in 2019/1/4 13:39
 * @description
 */
public class Result extends ResCode{
    /**
     * 人脸检测返回值
     */
    private FaceCheckFirst result;

    public FaceCheckFirst getResult() {
        return result;
    }

    public void setResult(FaceCheckFirst result) {
        this.result = result;
    }
}
