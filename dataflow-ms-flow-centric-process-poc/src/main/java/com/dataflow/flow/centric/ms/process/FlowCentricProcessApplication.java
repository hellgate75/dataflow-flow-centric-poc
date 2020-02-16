/**
 * 
 */
package com.dataflow.flow.centric.ms.process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@SpringBootApplication()
@EnableJpaRepositories(basePackages = "com.dataflow.flow.centric.lib.sql.repository.*")
@EntityScan("com.dataflow.*")
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class, WebMvcAutoConfiguration.class, MongoAutoConfiguration.class})
@RefreshScope
@ComponentScan(lazyInit = true, value={"com.dataflow.core.*","com.dataflow.flow.centric.lib.*", "com.dataflow.flow.centric.lib.*", "com.dataflow.flow.centric.ms.process.*" })
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
