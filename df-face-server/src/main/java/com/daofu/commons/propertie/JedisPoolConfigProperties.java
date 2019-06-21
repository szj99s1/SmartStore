package com.daofu.commons.propertie;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lc
 *
 */
@ConfigurationProperties(prefix = "df.redis.pool-config")
public class JedisPoolConfigProperties {
	private int maxTotal = 200;
	private int maxWaitMillis = 60000;

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	private Boolean testOnBorrow = true;
	private Boolean testWhileIdle = true;
	private int maxIdle = 300;
	private long timeBetweenEvictionRunsMillis = 1000;
	private int numTestsPerEvictionRun = 10;
	private int minEvictableIdleTimeMillis = 5000;

	
	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public Boolean getTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(Boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public int getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}

	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public int getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	@Override
	public String toString() {
		return "JedisPoolConfigProperties [maxTotal=" + maxTotal + ", maxWaitMillis=" + maxWaitMillis
				+ ", testOnBorrow=" + testOnBorrow + ", testWhileIdle=" + testWhileIdle + ", maxIdle=" + maxIdle
				+ ", timeBetweenEvictionRunsMillis=" + timeBetweenEvictionRunsMillis + ", numTestsPerEvictionRun="
				+ numTestsPerEvictionRun + ", minEvictableIdleTimeMillis=" + minEvictableIdleTimeMillis + "]";
	}

}
