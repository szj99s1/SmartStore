package com.daofu.faceserver.model.baidu;

/**
 * @author li-chuang
 * @date created in 2019/1/4 13:35
 * @description
 */
public class Gender {
    /**
     * male:男性 female:女性
     */
    private String type;
    /**
     * 性别置信度，范围【0~1】，0代表概率最小、1代表最大。
     */
    private String probability;
    /**
     * 0：男，1：女
     */
    private Integer sex;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}

