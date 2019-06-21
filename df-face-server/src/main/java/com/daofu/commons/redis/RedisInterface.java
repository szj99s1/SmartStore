package com.daofu.commons.redis;

/**
 * @author lichuang
 * @description
 * @date 2019-02-14 09:30
 */
public interface RedisInterface {
    /**
     * @description 加锁
     * @author lc
     * @date 2019-02-14 09:31
     * @param lockKey
     * @param requestId
     * @param expireTime
     * @return boolean
     */
    boolean lockAndRetry(String lockKey, String requestId, int expireTime);
    /**
     * @description 解锁
     * @author lc
     * @date 2019-02-14 10:18
     * @param lockKey
     * @param requestId
     * @return boolean
     */
    boolean unlock(String lockKey, String requestId);
}
