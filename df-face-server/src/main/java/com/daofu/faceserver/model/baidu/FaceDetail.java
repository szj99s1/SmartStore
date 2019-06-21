package com.daofu.faceserver.model.baidu;

/**
 * @author li-chuang
 * @date created in 2019/1/4 13:30
 * @description
 */
public class FaceDetail {
    /**
     * 人脸旋转角度参数
     */
    private Angle angle;
    /**
     * 年龄 ，当face_field包含age时返回
     */
    private Integer age;
    /**
     * 性别，face_field包含gender时返回
     */
    private Gender gender;

    public Angle getAngle() {
        return angle;
    }

    public void setAngle(Angle angle) {
        this.angle = angle;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
