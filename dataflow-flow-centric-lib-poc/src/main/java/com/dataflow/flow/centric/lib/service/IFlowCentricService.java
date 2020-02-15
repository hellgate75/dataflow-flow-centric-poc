/**
 * 
 */
package com.dataflow.flow.centric.lib.service;

import com.dataflow.flow.centric.lib.exceptions.IOFlowException;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public interface IFlowCentricService<T, R> {

	/**
	 * Defines operations per stream based on input data type and computing the response
	 * @param streamId Id of the stream, on the operation database
	 * @param templateName Name of the data template if known
	 * @param inputData Input Data elements
	 * @param arguments Optional arguments, known in the implementation
	 * @return (R) Computation Response Object
	 * @throws IOFlowException Exception arisen in case of any Stream computation error
	 */
	R computeStreamData(Long streamId, String templateName, T inputData, 
			Object... arguments)
					throws IOFlowException;

}
