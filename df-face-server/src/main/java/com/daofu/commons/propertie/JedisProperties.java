package com.daofu.commons.propertie;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author zwt
 *
 * @date 2018年3月23日 下午4:41:12 
 */
@ConfigurationProperties(prefix = "df.redis")
public class JedisProperties {
	private SingleRedis singleRedis = new JedisProperties.SingleRedis();
	private ClusterRedis clusterRedis = new JedisProperties.ClusterRedis();
	/**
	 * 是否开启集群配置，默认false
	 */
	private boolean openCluster = true;

	public boolean isOpenCluster() {
		return openCluster;
	}

	public void setOpenCluster(boolean openCluster) {
		this.openCluster = openCluster;
	}

	public ClusterRedis getClusterRedis() {
		return clusterRedis;
	}

	public void setClusterRedis(ClusterRedis clusterRedis) {
		this.clusterRedis = clusterRedis;
	}

	public SingleRedis getSingleRedis() {
		return singleRedis;
	}

	public void setSingleRedis(SingleRedis singleRedis) {
		this.singleRedis = singleRedis;
	}

	/**
	 * 单例
	 */
	public static class SingleRedis {
		private int port = 6379;

		private int timeout = 1000;

		private int database = 0;

		private String password;

		private String host;

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public int getTimeout() {
			return timeout;
		}

		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}

		public int getDatabase() {
			return database;
		}

		public void setDatabase(int database) {
			this.database = database;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}
	}

	/**
	 * 集群
	 */
	public static class ClusterRedis {
		private String password;
		private int connectionTimeout=2000;
		private int soTimeout=2000;
		private int maxAttempts=5;
		private List<String> nodes;

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public int getConnectionTimeout() {
			return connectionTimeout;
		}

		public void setConnectionTimeout(int connectionTimeout) {
			this.connectionTimeout = connectionTimeout;
		}

		public int getSoTimeout() {
			return soTimeout;
		}

		public void setSoTimeout(int soTimeout) {
			this.soTimeout = soTimeout;
		}

		public int getMaxAttempts() {
			return maxAttempts;
		}

		public void setMaxAttempts(int maxAttempts) {
			this.maxAttempts = maxAttempts;
		}

		public List<String> getNodes() {
			return nodes;
		}

		public void setNodes(List<String> nodes) {
			this.nodes = nodes;
		}

	}

}
