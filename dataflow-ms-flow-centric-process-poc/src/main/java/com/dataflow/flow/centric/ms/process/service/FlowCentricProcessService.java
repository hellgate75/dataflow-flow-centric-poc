/**
 * 
 */
package com.dataflow.flow.centric.ms.process.service;

import java.sql.Date;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.core.lib.logger.VlfLogger.Category;
import com.dataflow.flow.centric.lib.domain.ProcessedDataElement;
import com.dataflow.flow.centric.lib.domain.SourceDataElement;
import com.dataflow.flow.centric.lib.domain.metadata.Metadata;
import com.dataflow.flow.centric.lib.exceptions.FlowProcessException;
import com.dataflow.flow.centric.lib.exceptions.IOFlowException;
import com.dataflow.flow.centric.lib.helper.HQLHelper;
import com.dataflow.flow.centric.lib.helper.LoggerHelper;
import com.dataflow.flow.centric.lib.service.IFlowCentricService;
import com.dataflow.flow.centric.lib.sql.entity.FlowInputData;
import com.dataflow.flow.centric.lib.sql.entity.FlowProcessData;
import com.dataflow.flow.centric.lib.sql.repository.FlowInputDataRepository;
import com.dataflow.flow.centric.lib.sql.repository.FlowProcessDataRepository;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Component
@Configuration
public class FlowCentricProcessService implements IFlowCentricService<String, ProcessedDataElement> {
	
	@Value("${dataflow.flow.centric.model.field.names}")
	private String bSonModelFields;
	
	@Value("${dataflow.flow.centric.model.field.indexes}")
	private String bSonIndexFields;
	
	@Autowired
	protected VlfLogger vlfLogger;

	@Autowired
	protected FlowInputDataRepository flowInputDataRepository;
	
	@Autowired
	protected FlowProcessDataRepository flowProcessDataRepository;
	
	@Autowired
	protected KeyManagerService keyManagerService;
	
	/*
	 * Updating the MONITOR DB about success during processing
	 */
	private void updateSuccess(Long flowId, SourceDataElement inputDataRequest, FlowProcessData flowProcessData) {
		long currTime = System.currentTimeMillis();
		if ( flowProcessData != null ) {
			flowProcessData.setClosed(false);
			flowProcessData.setUpdatedTs(new Date(currTime));
			HQLHelper.saveAndRetryFlowProcessDataEntity(flowProcessDataRepository, flowProcessData, vlfLogger);
			if ( flowId == null || flowId <= 0 ) {
				flowId = flowProcessData.getInputId();
			}
		}
		if ( inputDataRequest != null ) {
			if ( flowId == null || flowId <= 0 ) {
				flowId = inputDataRequest.getFlowId(); 
			}
		}
		if ( flowId != null && flowId > 0 ) {
			FlowInputData flowInputData = HQLHelper.loadFlowInputDataEntity(flowInputDataRepository, flowId);
			if ( flowInputData != null ) {
				flowInputData.setClosed(false);
				flowInputData.setProcessed(true);
				flowInputData.setSinked(false);
				flowInputData.setUpdatedTs(new Date(currTime));
				HQLHelper.saveAndRetryFlowInputDataEntity(flowInputDataRepository, flowInputData, vlfLogger);
			}
		}
	}
	
	/*
	 * Updating the MONITOR DB about failure during processing
	 */
	private void updateFailure(Long flowId, SourceDataElement inputDataRequest, FlowProcessData flowProcessData) {
		long currTime = System.currentTimeMillis();
		if ( flowProcessData != null ) {
			flowProcessData.setClosed(true);
			flowProcessData.setUpdatedTs(new Date(currTime));
			HQLHelper.saveAndRetryFlowProcessDataEntity(flowProcessDataRepository, flowProcessData, vlfLogger);
			if ( flowId == null || flowId <= 0 ) {
				flowId = flowProcessData.getInputId();
			}
		}
		if ( inputDataRequest != null ) {
			if ( flowId == null || flowId <= 0 ) {
				flowId = inputDataRequest.getFlowId(); 
			}
		}
		if ( flowId != null && flowId > 0 ) {
			FlowInputData flowInputData = HQLHelper.loadFlowInputDataEntity(flowInputDataRepository, flowId);
			if ( flowInputData != null ) {
				flowInputData.setClosed(true);
				flowInputData.setProcessed(false);
				flowInputData.setSinked(false);
				flowInputData.setUpdatedTs(new Date(currTime));
				HQLHelper.saveAndRetryFlowInputDataEntity(flowInputDataRepository, flowInputData, vlfLogger);
			}
		}
	}
	
	@Override
	public ProcessedDataElement computeStreamData(Long flowId, String modelType, String inputData, Object... arguments)
			throws IOFlowException {
		SourceDataElement inputDataRequest = null;
		FlowProcessData flowProcessData = null;
		try {
			inputDataRequest = (SourceDataElement)arguments[0];
			flowProcessData = (FlowProcessData)arguments[1];
			Metadata bsonMetadata = Metadata.metaOf(inputDataRequest.getBsonObject(), inputDataRequest.getModelType());
			// In case of no parse or no exception trigger the call of a null pointer
			// object will cause an exception, else-wise we have a log record in info
			// logging level,
			
			modelType = bSonModelFields != null && ! bSonModelFields.trim().isEmpty() ? 
										keyManagerService.collectModelFrom(inputDataRequest.getBsonObject(), Arrays.asList(bSonModelFields.split(","))) :
										"unknown";
			if ( modelType == null || bSonModelFields.trim().isEmpty() ) {
				modelType = "unknown";
			}
			String index = bSonIndexFields != null && ! bSonIndexFields.trim().isEmpty() ?  
					keyManagerService.collectIndexesFrom(inputDataRequest.getBsonObject(), Arrays.asList(bSonIndexFields.split(","))) : 
					"";
			LoggerHelper.logInfo(vlfLogger, "FlowCentricSourceService::computeStreamData", 
					String.format("Metadata Object generated successfullyL %s", bsonMetadata.toJson()));
			String noSqlCollection = "flow-centric-" + modelType.toLowerCase();
			updateSuccess(flowId, inputDataRequest, flowProcessData);
			return new ProcessedDataElement(flowId, flowProcessData.getId(), modelType, index, noSqlCollection, inputDataRequest.getBsonObject(), bsonMetadata);
		} catch (Exception e) {
			String message = String.format("Unable to execute request for Stream Data Processing Service -> error (%s) message: %s -> input : %s", e.getClass().getName(), e.getMessage(), inputData);
			LoggerHelper.logError(vlfLogger, "FlowCentricSourceService::computeStreamData", message, Category.BUSINESS_ERROR, e);
			throw new FlowProcessException(message, e);
		}
		finally {
			updateFailure(flowId, inputDataRequest, flowProcessData);
		}
	}

}
