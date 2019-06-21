package com.daofu.commons.bean.redis;

import com.daofu.commons.propertie.JedisProperties;
import com.daofu.commons.redis.RedisClient;
import com.daofu.commons.redis.RedisClientSingle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author lc
 *
 */
@Configuration
@EnableConfigurationProperties(value = { JedisProperties.class })
public class JedisConfig {
    @Autowired
    private JedisProperties jedisProperties;
    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    @Bean
    public JedisPool jedisPool() {
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, jedisProperties.getSingleRedis().getHost(),
                jedisProperties.getSingleRedis().getPort(), jedisProperties.getSingleRedis().getTimeout(),
                jedisProperties.getSingleRedis().getPassword(), jedisProperties.getSingleRedis().getDatabase());
        return jedisPool;
    }

    @Bean
    public RedisClient redisClient() {
        return new RedisClientSingle();
    }

}
