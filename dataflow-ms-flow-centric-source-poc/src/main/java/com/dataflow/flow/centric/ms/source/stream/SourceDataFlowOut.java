/**
 * 
 */
package com.dataflow.flow.centric.ms.source.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public interface SourceDataFlowOut {

	/**
	 * Name of the output channel.
	 */
	String OUTPUT = "sourceDataFlowOut";

	/**
	 * @return output channel
	 */
	@Output(SourceDataFlowOut.OUTPUT)
	MessageChannel output();

}
