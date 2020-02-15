/**
 * 
 */
package com.dataflow.flow.centric.ms.source.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories
@EnableTransactionManagement
@EnableBinding(Source.class)
@Component
public class FlowCentricSource {
	
	@Autowired
	public Source source;
	
}
