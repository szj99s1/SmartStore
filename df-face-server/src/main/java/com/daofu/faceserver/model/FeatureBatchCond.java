package com.daofu.faceserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

/**
 * @author lichuang
 * @description
 * @date 2019-05-06 16:48
 */
@Data
@AllArgsConstructor
public class FeatureBatchCond {
    private Integer compareScore;
    private String feature;
    private Collection<String> featureList;
}
