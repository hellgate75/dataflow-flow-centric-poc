/**
 * 
 */
package com.dataflow.flow.centric.lib.stream;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public interface FileItemTransformer<T, K> extends Transformer<T, K> {

	/**
	 * @param processId
	 * @param processName
	 * @param fileName
	 * @param fileType
	 */
	void init(Long processId, String processName, String fileName, String fileType);
}
