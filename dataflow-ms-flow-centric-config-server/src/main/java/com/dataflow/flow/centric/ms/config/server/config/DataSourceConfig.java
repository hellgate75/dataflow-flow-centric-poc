package com.dataflow.flow.centric.ms.config.server.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h1>DataSourceConfig</h1> Define DataSource properties
 */
@Configuration
@ConfigurationProperties("dataflow.app.config.server")
public class DataSourceConfig {

	@Autowired
	private ConfigServerConfig configServerConfig;

	/**
	 * <h2>dataSourceWorkflowDatabase</h2> 
	 * @return dataSource
	 * 
	 */
	@Bean(name = { "dataSource" })
	public DataSource dataSourceWorkflowDatabase() {
		org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
		dataSource.setDriverClassName(configServerConfig.getDriverClassName());
		dataSource.setUrl(configServerConfig.getDbUrl());
		dataSource.setUsername(configServerConfig.getU());
		dataSource.setPassword(configServerConfig.getP());
		dataSource.setInitialSize(configServerConfig.getInitialSize());
		dataSource.setMaxActive(configServerConfig.getMaxActive());
		dataSource.setMaxIdle(configServerConfig.getMaxIdle());
		dataSource.setMinIdle(configServerConfig.getMinIdle());
		return dataSource;
	}

}
