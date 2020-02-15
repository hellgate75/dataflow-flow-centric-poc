/**
 * 
 */
package com.dataflow.flow.centric.ms.source.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public interface SourceDataFlowIn {
	/**
	 * Input channel name.
	 */
	String INPUT = "sourceDataFlowIn";

	/**
	 * @return input channel.
	 */
	@Input(SourceDataFlowIn.INPUT)
	SubscribableChannel input();

}
