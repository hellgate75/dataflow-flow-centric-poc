package com.dataflow.flow.centric.ms.config.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <h1>ConfigServerConfig</h1>Config server DB Configuration
 */
@Component
@ConfigurationProperties(prefix = "dataflow.app.config.server")
public class ConfigServerConfig {
	
	private String u;
	private String p;
	private String driverClassName;
	private String dbUrl;
	private Integer initialSize;
	private Integer maxActive;
	private Integer maxIdle;
	private Integer minIdle;
	private String appFolder;
	
	/**
	 * @return the u
	 */
	public String getU() {
		return u;
	}
	/**
	 * @param u the u to set
	 */
	public void setU(String u) {
		this.u = u;
	}
	/**
	 * @return the p
	 */
	public String getP() {
		return p;
	}
	/**
	 * @param p the p to set
	 */
	public void setP(String p) {
		this.p = p;
	}
	/**
	 * @return the driverClassName
	 */
	public String getDriverClassName() {
		return driverClassName;
	}
	/**
	 * @param driverClassName the driverClassName to set
	 */
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	/**
	 * @return the dbUrl
	 */
	public String getDbUrl() {
		return dbUrl;
	}
	/**
	 * @param dbUrl the dbUrl to set
	 */
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	/**
	 * @return the initialSize
	 */
	public Integer getInitialSize() {
		return initialSize;
	}
	/**
	 * @param initialSize the initialSize to set
	 */
	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}
	/**
	 * @return the maxActive
	 */
	public Integer getMaxActive() {
		return maxActive;
	}
	/**
	 * @param maxActive the maxActive to set
	 */
	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}
	/**
	 * @return the maxIdle
	 */
	public Integer getMaxIdle() {
		return maxIdle;
	}
	/**
	 * @param maxIdle the maxIdle to set
	 */
	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}
	/**
	 * @return the minIdle
	 */
	public Integer getMinIdle() {
		return minIdle;
	}
	/**
	 * @param minIdle the minIdle to set
	 */
	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}
	/**
	 * @return the appFolder
	 */
	public String getAppFolder() {
		return appFolder;
	}
	/**
	 * @param appFolder the appFolder to set
	 */
	public void setAppFolder(String appFolder) {
		this.appFolder = appFolder;
	}


}
