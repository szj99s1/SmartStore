package com.daofu.commons.bean.redis;

import com.daofu.commons.propertie.JedisPoolConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author zwt
 *
 * @date 2018年3月23日 下午4:41:00 
 */
@Configuration
@EnableConfigurationProperties(value = { JedisPoolConfigProperties.class })
public class JedisPoolConf {
	@Autowired
	private JedisPoolConfigProperties jedisPoolConfigProperties;

	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		redis.clients.jedis.JedisPoolConfig jedisPoolConfig = new redis.clients.jedis.JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(jedisPoolConfigProperties.getMaxTotal());
		jedisPoolConfig.setTestOnBorrow(jedisPoolConfigProperties.getTestOnBorrow());
		jedisPoolConfig.setMaxWaitMillis(jedisPoolConfigProperties.getMaxWaitMillis());
		jedisPoolConfig.setMaxIdle(jedisPoolConfigProperties.getMaxIdle());
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(jedisPoolConfigProperties.getTimeBetweenEvictionRunsMillis());
		jedisPoolConfig.setNumTestsPerEvictionRun(jedisPoolConfigProperties.getNumTestsPerEvictionRun());
		jedisPoolConfig.setMinEvictableIdleTimeMillis(jedisPoolConfigProperties.getMinEvictableIdleTimeMillis());
		jedisPoolConfig.setTestWhileIdle(jedisPoolConfigProperties.getTestWhileIdle());
		return jedisPoolConfig;
	}

}
