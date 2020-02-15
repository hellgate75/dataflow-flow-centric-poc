/**
 * 
 */
package com.dataflow.flow.centric.ms.source.controller;

import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.ServiceActivator;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.core.lib.logger.VlfLogger.Category;
import com.dataflow.flow.centric.lib.components.FlowCentricConfig;
import com.dataflow.flow.centric.lib.domain.SourceDataElement;
import com.dataflow.flow.centric.lib.exceptions.IOFlowException;
import com.dataflow.flow.centric.lib.helper.HQLHelper;
import com.dataflow.flow.centric.lib.helper.LoggerHelper;
import com.dataflow.flow.centric.lib.service.IFlowCentricService;
import com.dataflow.flow.centric.lib.sql.entity.FlowInputData;
import com.dataflow.flow.centric.lib.sql.repository.FlowInputDataRepository;
import com.dataflow.flow.centric.ms.source.stream.SourceDataFlowIn;

/**
 * @author Administrator
 *
 */
@EnableBinding({Source.class, SourceDataFlowIn.class})
public class FlowCentricSourceStreamController {
	
	@Value("${dataflow.flow.centric.model.field.name}")
	private String bSonModelField;

	
	@Autowired
	protected FlowInputDataRepository flowInputDataRepository;
	
	@Autowired
	protected FlowCentricConfig flowCentricConfig;
	
	@Autowired
	protected VlfLogger vlfLogger;
	
	@Autowired
	protected IFlowCentricService<String, SourceDataElement> flowCentricSourceService;
	
	@ServiceActivator(inputChannel=SourceDataFlowIn.INPUT,
						outputChannel = Source.OUTPUT,
						requiresReply = "true",
						autoStartup = "true", 
						async = "true")
	public SourceDataElement interceptSourceDataFlowRowEntity(String inputData) {
		Long streamId = null;
		try {
			String type = null;
			FlowInputData data = flowCentricConfig.createAndSaveNewFlowInputData(flowInputDataRepository,inputData, type);
			streamId = data.getId();
			return flowCentricSourceService.computeStreamData(data.getId(), type, inputData, bSonModelField);
		} catch (IOFlowException e) {
			String message = String.format("Unable to process Input Data for Stream Data Processing Service -> error message: (%s) -> %s, data = %s", e.getClass().getName(), e.getMessage(), inputData);
			LoggerHelper.logError(vlfLogger, "FlowCentricSourceStreamController::computeStreamData", message, Category.BUSINESS_ERROR, e);
			if ( streamId != null ) {
				// Some operations written into the operational Database. 
				// Roll-back executed by closing the FlowDataInputm so no processing is expected.
				Optional<FlowInputData> dataOpt = flowInputDataRepository.findById(streamId);
				if ( dataOpt.isPresent() ) {
					FlowInputData flowInputData = dataOpt.get();
					if ( flowInputData != null ) {
						flowInputData.setClosed(true);
						flowInputData.setUpdatedTs(new Date(System.currentTimeMillis()));
						HQLHelper.saveAndRetryFlowInputDataEntity(flowInputDataRepository, flowInputData, vlfLogger);
					}
				}
			}
			// Null responses are rejected and a log will be automatically written by SpringDataflow
			// We can even write a message handler, in order to manage by ourselves the event.
			return null;
		}
	}

}