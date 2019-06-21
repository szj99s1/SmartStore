package com.daofu.faceserver.model;

import com.daofu.commons.utils.DateUtils;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author li-chuang
 * @date created in 2018/12/24 15:54
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SmsNotifyCond implements Serializable {
    private static final long serialVersionUID = -7704772932808777700L;
    /**
     * deviceId
     */
    @SerializedName("device_id")
    private String deviceId;
    /**
     * 识别记录url
     */
    private String imageUrl;
    /**
     * 备注
     */
    private String remark;
    /**
     * 上次到访时间
     */
    private String stamp;
    /**
     * id
     */
    private String id;
    /**
     * openType
     */
    @SerializedName("open_type")
    private Integer openType;
    /**
     * sign
     */
    private String sign;
    /**
     * productid
     */
    @SerializedName("productid")
    private String productId;

    public void setStamp(Date date){
        this.stamp = date == null ? null : DateUtils.dateFormart(date);
    }
}
