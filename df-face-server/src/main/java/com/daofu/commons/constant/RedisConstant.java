package com.daofu.commons.constant;

/**
 * @author li-chuang
 * @date created in 2018/12/14 17:24
 * @description
 */
public final class RedisConstant {
    public static final String OK = "OK";
    public static final String NX = "NX";
    public static final String EX = "EX";
    public static final int SIXTY = 60;
    public static final String WECHAT_ACCESS_TOKEN = "wx:accesstoken";
    public static final String WECHAT_SUBSCRIPTION_LOGIN_TOKEN = "wechat:subscription:login:token";
    public static final String UN_LOCK = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
}
