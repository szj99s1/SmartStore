package com.daofu.faceserver.model;

import java.math.BigDecimal;

/**
 * @author lichuang
 * @description
 * @date 2019-01-23 14:18
 */
public class FaceOther {
    private Integer gender;
    private Integer age;
    private BigDecimal yaw;
    private BigDecimal pitch;
    private BigDecimal roll;

    @Override
    public String toString() {
        return "FaceOther{" +
                "gender=" + gender +
                ", age=" + age +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                ", roll=" + roll +
                '}';
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BigDecimal getYaw() {
        return yaw;
    }

    public void setYaw(BigDecimal yaw) {
        this.yaw = yaw;
    }

    public BigDecimal getPitch() {
        return pitch;
    }

    public void setPitch(BigDecimal pitch) {
        this.pitch = pitch;
    }

    public BigDecimal getRoll() {
        return roll;
    }

    public void setRoll(BigDecimal roll) {
        this.roll = roll;
    }
}
