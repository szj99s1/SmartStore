package com.daofu.commons.utils;

import java.util.UUID;

/**
 * @author li-chuang
 * @date created in 2018/12/14 18:02
 * @description
 */
public final class DataUtils {
    private DataUtils() {}

    public static String getUuid(){
        return UUID.randomUUID().toString().toLowerCase().replaceAll("-", "");
    }

    public static String getRandam(){
        return String.valueOf((int)(Math.random() * 89999) + 10000);
    }
}
