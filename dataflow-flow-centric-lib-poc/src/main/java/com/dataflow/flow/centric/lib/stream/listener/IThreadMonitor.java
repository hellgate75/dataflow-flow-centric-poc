/**
 * 
 */
package com.dataflow.flow.centric.lib.stream.listener;

import java.util.UUID;

import com.dataflow.flow.centric.lib.stream.domain.ProcessType;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public interface IThreadMonitor {
	
	/**
	 * @param processType
	 * @param threadName
	 * @param processId
	 * @param processName
	 * @param groupId
	 * @return
	 */
	UUID threadStarted(ProcessType processType, String threadName, Long processId, String processName, Integer groupId);
	/**
	 * @param threadUUID
	 */
	void threadStopped(UUID threadUUID);
}
