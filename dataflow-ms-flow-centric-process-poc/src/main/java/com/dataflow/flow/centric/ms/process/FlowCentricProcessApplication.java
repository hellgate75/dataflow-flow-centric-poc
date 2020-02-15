/**
 * 
 */
package com.dataflow.flow.centric.ms.process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@SpringBootApplication()
@EnableJpaRepositories(basePackages = "com.dataflow.flow.centric.lib.sql.repository.*")
@EntityScan("com.dataflow.*")
@ComponentScan({"com.dataflow.core.*","com.dataflow.flow.centric.lib.sql.entity.*", "com.dataflow.flow.centric.lib.*", "com.dataflow.flow.centric.ms.process.*" })
@RefreshScope
@EnableScheduling
public class FlowCentricProcessApplication {

	/**
	 * This method is responsible for boot the application
	 * 
	 * @param args
	 *            The runtime arguments are passed to the spring boot
	 *            application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(FlowCentricProcessApplication.class, args);
	}

}
