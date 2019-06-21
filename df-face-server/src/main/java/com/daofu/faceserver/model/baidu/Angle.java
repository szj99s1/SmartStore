package com.daofu.faceserver.model.baidu;

import java.math.BigDecimal;

/**
 * @author li-chuang
 * @date created in 2019/1/4 13:31
 * @description
 */
public class Angle {
    /**
     * 三维旋转之俯仰角度[-90(上), 90(下)]
     */
    private BigDecimal pitch;
    /**
     * 三维旋转之左右旋转角[-90(左), 90(右)]
     */
    private BigDecimal yaw;
    /**
     * 平面内旋转角[-180(逆时针), 180(顺时针)]
     */
    private BigDecimal roll;

    public BigDecimal getPitch() {
        return pitch;
    }

    public void setPitch(BigDecimal pitch) {
        this.pitch = pitch;
    }

    public BigDecimal getYaw() {
        return yaw;
    }

    public void setYaw(BigDecimal yaw) {
        this.yaw = yaw;
    }

    public BigDecimal getRoll() {
        return roll;
    }

    public void setRoll(BigDecimal roll) {
        this.roll = roll;
    }
}
