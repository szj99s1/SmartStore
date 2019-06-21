package com.daofu.faceserver.model.baidu;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author li-chuang
 * @date created in 2019/1/4 13:23
 * @description
 */
public class ResCode {
    /**
     * 返回消息code
     */
    @JSONField(name = "error_code")
    private Integer errorCode;
    /**
     * 返回错误信息
     */
    @JSONField(name = "error_msg")
    private String errorMsg;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
