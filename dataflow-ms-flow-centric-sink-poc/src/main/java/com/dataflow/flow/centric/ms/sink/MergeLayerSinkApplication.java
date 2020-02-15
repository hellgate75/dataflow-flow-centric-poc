/**
 * 
 */
package com.dataflow.flow.centric.ms.sink;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@SpringBootApplication()
@EnableJpaRepositories(basePackages = "com.vzw.*")
@EntityScan("com.vzw.*")
@ComponentScan("com.vzw.*,com.vzw.vdp.*,com.vzw.vlf.*,com.dataflow.*,com.dataflow.lib.merge.layer.service.sink.entity.*")
@RefreshScope
public class MergeLayerSinkApplication {
	/**
	 * This method is responsible for boot the application
	 * 
	 * @param args
	 *            The runtime arguments are passed to the spring boot
	 *            application.
	 * @throws InterruptedException
	 *             the InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		SpringApplication.run(MergeLayerSinkApplication.class, args);
	}

}
