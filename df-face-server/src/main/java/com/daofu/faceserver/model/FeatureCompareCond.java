package com.daofu.faceserver.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author lichuang
 * @description
 * @date 2019-03-13 13:19
 */
@Data
public class FeatureCompareCond {
    /**
     * 特征值
     */
    @NotBlank(message = "特征值不能为空！")
    private String feature0;
    /**
     * 特征值
     */
    @NotBlank(message = "特征值不能为空！")
    private String feature1;
    /**
     * 唯一id
     */
    @NotBlank(message = "唯一id不能为空！")
    private String recId;
}
