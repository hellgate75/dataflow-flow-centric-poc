/**
 * 
 */
package com.dataflow.flow.centric.lib.helper;

import java.sql.Clob;
import java.util.Optional;

import javax.sql.rowset.serial.SerialClob;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.core.lib.logger.VlfLogger.Category;
import com.dataflow.flow.centric.lib.sql.entity.FlowInputData;
import com.dataflow.flow.centric.lib.sql.entity.FlowProcessData;
import com.dataflow.flow.centric.lib.sql.repository.FlowInputDataRepository;
import com.dataflow.flow.centric.lib.sql.repository.FlowProcessDataRepository;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public final class HQLHelper {
	
	private HQLHelper() {
		super();
	}

	
	public static final Clob newClobFromText(String text) {
		if ( text == null )
			return null;
		try {
			return new SerialClob(text.toCharArray());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param flowInputDataRepository
	 * @param flowId
	 * @return
	 */
	public static final FlowInputData loadFlowInputDataEntity(FlowInputDataRepository flowInputDataRepository, Long flowId) {
		return loadFlowInputDataEntity(flowInputDataRepository, flowId, MAX_RETRY_NO);
	}

	/**
	 * @param flowInputDataRepository
	 * @param flowId
	 * @return
	 */
	private static final FlowInputData loadFlowInputDataEntity(FlowInputDataRepository flowInputDataRepository, Long flowId, int countdown) {
		Optional<FlowInputData> entityOptional = flowInputDataRepository.findById(flowId);
		if ( ! entityOptional.isPresent() ) {
			if ( countdown > 0 ) {
				return loadFlowInputDataEntity(flowInputDataRepository, flowId, countdown-1);
				
			}
			return null;
		}
		return entityOptional.get();
	}

	/**
	 * @param flowProcessDataRepository
	 * @param processId
	 * @return
	 */
	public static final FlowProcessData loadFlowProcessDataEntity(FlowProcessDataRepository flowProcessDataRepository, Long processId) {
		return loadFlowProcessDataEntity(flowProcessDataRepository, processId, MAX_RETRY_NO);
	}

	/**
	 * @param flowProcessDataRepository
	 * @param processId
	 * @return
	 */
	private static final FlowProcessData loadFlowProcessDataEntity(FlowProcessDataRepository flowProcessDataRepository, Long processId, int countdown) {
		Optional<FlowProcessData> entityOptional = flowProcessDataRepository.findById(processId);
		if ( ! entityOptional.isPresent() ) {
			if ( countdown > 0 ) {
				return loadFlowProcessDataEntity(flowProcessDataRepository, processId, countdown-1);
				
			}
			return null;
		}
		return entityOptional.get();
	}

	public static int MAX_RETRY_NO = 5;
	
	/**
	 * Default Retry Value {@link HQLHelper#MAX_RETRY_NO}, and it's customizable on runtime
	 * @param flowInputDataRepository
	 * @param flowInputData
	 * @param vlfLogger
	 * @return
	 */
	public static final FlowInputData saveAndRetryFlowInputDataEntity(FlowInputDataRepository flowInputDataRepository, FlowInputData flowInputData, VlfLogger vlfLogger) {
		return saveAndRetryFlowInputDataEntity(flowInputDataRepository, flowInputData, MAX_RETRY_NO, vlfLogger);
	}		
		
	
	/**
	 * Default Retry Value {@link HQLHelper#MAX_RETRY_NO}, and it's customizable on runtime
	 * @param flowInputDataRepository
	 * @param flowInputData
	 * @param maxRetryNo
	 * @param vlfLogger
	 * @return
	 */
	public static final FlowInputData saveAndRetryFlowInputDataEntity(FlowInputDataRepository flowInputDataRepository, FlowInputData flowInputData, int maxRetryNo, VlfLogger vlfLogger) {
		if ( flowInputData == null )
			return null;
		boolean success = false;
		int counter = 0;
		while ( ( !success || flowInputData == null) && counter < maxRetryNo) {
			try {
				counter++;
				flowInputData = flowInputDataRepository.save(flowInputData);
				success = true;
			} catch (Exception e) {
				LoggerHelper.logError(vlfLogger, "HQLHelper::saveAndRetryFlowInputDataEntity", 
						String.format("Error inserting/updating task entity : flow id: %s, type: %s",
								""+ flowInputData.getId(),
								flowInputData.getTypeName()), 
						Category.BUSINESS_ERROR, e);
				success = false;
			}
		}
		if ( !success || flowInputData == null ) {
			LoggerHelper.logError(vlfLogger, "HQLHelper::saveAndRetryMergeTasksEntity", 
					String.format("Abandoned insert/update task entity : flow id: %s, type: %s - Reached max retry count : %s",
							""+ flowInputData.getId(),
							flowInputData.getTypeName(), 
							"" + maxRetryNo),
					Category.BUSINESS_ERROR, null);
		}
		return flowInputData;
	}

	/**
	 * Default Retry Value {@link HQLHelper#MAX_RETRY_NO}, and it's customizable on runtime
	 * @param flowProcessDataRepository
	 * @param flowProcessData
	 * @param vlfLogger
	 * @return
	 */
	public static final FlowProcessData saveAndRetryFlowProcessDataEntity(FlowProcessDataRepository flowProcessDataRepository, FlowProcessData flowProcessData, VlfLogger vlfLogger) {
		return saveAndRetryFlowProcessDataEntity(flowProcessDataRepository, flowProcessData, MAX_RETRY_NO, vlfLogger);
	}
	
	/**
	 * Default Retry Value {@link HQLHelper#MAX_RETRY_NO}, and it's customizable on runtime
	 * @param flowProcessDataRepository
	 * @param flowProcessData
	 * @param maxRetryNo
	 * @param vlfLogger
	 * @return
	 */
	public static final FlowProcessData saveAndRetryFlowProcessDataEntity(FlowProcessDataRepository flowProcessDataRepository, FlowProcessData flowProcessData, int maxRetryNo, VlfLogger vlfLogger) {
		if ( flowProcessData == null )
			return null;
		boolean success = false;
		int counter = 0;
		while ( ( !success || flowProcessData == null) && counter < maxRetryNo) {
			try {
				counter++;
				flowProcessData = flowProcessDataRepository.save(flowProcessData);
				success = true;
			} catch (Exception e) {
				LoggerHelper.logError(vlfLogger, "HQLHelper::saveAndRetryMergeProcessEntity", 
						String.format("Error inserting/updating process entity : process id: %s, stream: %s, collection: %s",
								""+ flowProcessData.getId(),
								"" + flowProcessData.getInputId(),
								flowProcessData.getCollectionName()), 
						Category.BUSINESS_ERROR, e);
				success = false;
			}
		}
		if ( !success || flowProcessData == null ) {
			LoggerHelper.logError(vlfLogger, "HQLHelper::saveAndRetryMergeProcessEntity", 
					String.format("Abandoned insert/update process entity : process id: %s, stream: %s, collection: %s - Reached max retry count : %s",
							""+ flowProcessData.getId(),
							"" + flowProcessData.getInputId(),
							flowProcessData.getCollectionName(), 
							"" + maxRetryNo), 
					Category.BUSINESS_ERROR, null);
		}
		return flowProcessData;
	}

}
