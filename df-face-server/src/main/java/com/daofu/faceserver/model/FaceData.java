package com.daofu.faceserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author lichuang
 * @description
 * @date 2019-02-11 16:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FaceData {
    private Integer score;
    private Integer type;
    private String userId;
    private String feature;
    /**
     * 回头客天数
     */
    private Integer strangerOverDay;
}
