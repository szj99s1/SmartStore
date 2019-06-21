package com.daofu.util;

import org.apache.commons.configuration.PropertiesConfiguration;


public class Configuration {

    private static PropertiesConfiguration config = PropertiesLoader.getConfiguration("conf");

    public static int getInt(String key) {
        return config.getInt(key, 0);
    }

    public static String getString(String key) {
        return config.getString(key, "");
    }

    public static boolean getBoolean(String key) {
        return config.getBoolean(key, false);
    }

}
