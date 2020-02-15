/**
 * 
 */
package com.dataflow.flow.centric.ms.sink.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public interface FlowCentricSinkOut {

	/**
	 * Name of the output channel.
	 */
	String OUTPUT = "sinkExternalTopicsOut";

	/**
	 * @return output channel
	 */
	@Output(FlowCentricSinkOut.OUTPUT)
	MessageChannel output();

}
