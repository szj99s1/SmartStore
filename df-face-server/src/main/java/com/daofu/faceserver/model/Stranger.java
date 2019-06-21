package com.daofu.faceserver.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author lichuang
 * @description
 * @date 2019-02-13 13:23
 */
@Data
@Accessors(chain = true)
public class Stranger {
    /**
     * 主键自增
     */
    private Integer id;
    /**
     * 店铺id
     */
    private Integer shopId;
    /**
     * 特殊值
     */
    private String feature;
    /**
     * 上次到访时间
     */
    private Date visittime;
    /**
     * 陌生人id
     */
    private String recId;
    /**
     * 识别图片
     */
    private String faceUrl;
    /**
     * 性别（0：男，1：女）
     */
    private Integer sex;
    /**
     * 备注
     */
    private String memo;
}
