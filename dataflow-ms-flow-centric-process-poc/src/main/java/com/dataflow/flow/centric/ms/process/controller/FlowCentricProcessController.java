/**
 * 
 */
package com.dataflow.flow.centric.ms.process.controller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.ServiceActivator;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.core.lib.logger.VlfLogger.Category;
import com.dataflow.core.lib.logger.VlfLogger.Status;
import com.dataflow.flow.centric.lib.components.FlowCentricConfig;
import com.dataflow.flow.centric.lib.constants.GlobalConstants;
import com.dataflow.flow.centric.lib.domain.ProcessedDataElement;
import com.dataflow.flow.centric.lib.domain.SourceDataElement;
import com.dataflow.flow.centric.lib.helper.HQLHelper;
import com.dataflow.flow.centric.lib.helper.LoggerHelper;
import com.dataflow.flow.centric.lib.service.IFlowCentricService;
import com.dataflow.flow.centric.lib.sql.entity.FlowInputData;
import com.dataflow.flow.centric.lib.sql.entity.FlowProcessData;
import com.dataflow.flow.centric.lib.sql.repository.FlowInputDataRepository;
import com.dataflow.flow.centric.lib.sql.repository.FlowProcessDataRepository;
import com.dataflow.flow.centric.lib.stream.domain.ProcessType;
import com.dataflow.flow.centric.lib.stream.listener.IThreadMonitor;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = {FlowInputDataRepository.class, FlowProcessDataRepository.class})
@EnableBinding(Processor.class)
public class FlowCentricProcessController implements IThreadMonitor {

	public static final Map<ProcessType, ConcurrentHashMap<UUID, String>> THREADS_MAP = new ConcurrentHashMap<>(0);
	
	@Autowired
	protected FlowCentricConfig flowCentricConfig;

	@Autowired
	protected FlowInputDataRepository flowInputDataRepository;
	
	@Autowired
	protected FlowProcessDataRepository flowProcessDataRepository;
	
	@Autowired
	protected VlfLogger vlfLogger;
	
	@Autowired
	protected IFlowCentricService<String, ProcessedDataElement> flowCentricProcessService;
	
	@Value("${dataflow.core.logger.logLevel}")
	private VlfLogger.LogLevel logLevel;
	
	@PostConstruct
	protected void initBean() {
		vlfLogger.newLogSet(GlobalConstants.SERVICE_ID, logLevel);
		this.vlfLogger.write(GlobalConstants.LOG_OPERATION_PROCESS_LISTENER, GlobalConstants.LOG_TYPE_LISTENER,
				VlfLogger.Severity.INFO, "INIT :: SPRING CLOUD DATA FLOW -> PROCESS", VlfLogger.LogLevel.INFO);
		for ( ProcessType type: ProcessType.values() ) {
			if ( ! THREADS_MAP.containsKey(type) ) {
				THREADS_MAP.put(type, new ConcurrentHashMap<UUID, String>(0));
			}
		}
	}
	

	@PreDestroy
	protected void destroyBean() {
		this.vlfLogger.write(GlobalConstants.LOG_OPERATION_PROCESS_LISTENER, GlobalConstants.LOG_TYPE_LISTENER,
				VlfLogger.Severity.INFO, "DESTROY :: SPRING CLOUD DATA FLOW -> PROCESS", VlfLogger.LogLevel.INFO);
		vlfLogger.endLogSet(Status.SUCCESS, Category.SUCCESS, "200", "DESTROY :: SPRING CLOUD DATA FLOW -> PROCESS");
	}

	@Override
	public UUID threadStarted(ProcessType processType, String threadName, Long processId, String processName,
			Integer groupId) {
		UUID uuid = UUID.randomUUID();
		THREADS_MAP.get(processType).put(uuid, String.format("Process (type: %s) uuid: %s, thread name: %s, process id: %s, process name: %s, group id : %s",
														processType.name(), uuid.toString(), threadName, "" + processId, processName, "" + groupId));
		return uuid;
	}
	@Override
	public void threadStopped(UUID threadUUID) {
		if ( threadUUID == null )
			return;
		Optional<ProcessType> typeOpt = THREADS_MAP.entrySet().stream()
				.filter( es -> es.getValue().containsKey(threadUUID) )
				.map( es -> es.getKey())
				.findFirst();
			if ( typeOpt.isPresent() ) {
				THREADS_MAP.get(typeOpt.get()).remove(threadUUID);
			}
	}

	@ServiceActivator(inputChannel=Processor.INPUT,
			outputChannel = Processor.OUTPUT,
			requiresReply = "true",
			autoStartup = "true", 
			async = "true")
	public String processSourceData(String inputText) {
		if ( inputText == null || inputText.trim().isEmpty() ) {
			LoggerHelper.logWarning(vlfLogger, "FlowCentricProcessController::processSourceData", "Recovered an empty or null message: <" + inputText + ">", null);
			return "";
		}
		Long flowId = 0l;
		String templateName = "";
		Long processId = 0l;
		FlowProcessData flowProcessData = null;
		UUID threadUUID = null ;
		try {
			SourceDataElement inputData = SourceDataElement.fromJson(inputText);
			flowId = inputData.getFlowId();
			String collection = "";
			LoggerHelper.logInfo(vlfLogger, "FlowCentricProcessController::processSourceData", "Read new source element: " + inputData);
			flowProcessData = flowCentricConfig.createAndSaveNewFlowProcessData(flowProcessDataRepository, flowId, collection, null);
			if ( flowProcessData != null ) {
				threadUUID = threadStarted(ProcessType.PROCESS, "FlowCentricProcessSourceData-"+UUID.randomUUID().toString(), flowProcessData.getId(), "FlowCentricProcessSourceData-" + UUID.randomUUID().toString(), 0);
				LoggerHelper.logInfo(vlfLogger, "FlowCentricProcessController::processSourceData", "Generated new process data element: " + flowProcessData);
			}
			else
				LoggerHelper.logWarning(vlfLogger, "FlowCentricProcessController::processSourceData", "Unable to generate new process data: continue without process monitoring!!", null);
			templateName = inputData.getModelType();
			processId=flowProcessData.getId();
			ProcessedDataElement response = flowCentricProcessService.computeStreamData(flowId, templateName, inputData.getBsonObject().toJson(), inputData, flowProcessData);
			markFlowAndProcessAsSuccess(flowId);
			return response.toJson();
		} catch (Exception e) {
			String errorMessage = String.format("Error dunring computing source data from the request body: %s",
					""+inputText);
			LoggerHelper.logError(vlfLogger, "FlowCentricProcessController::processSourceData", errorMessage, Category.BUSINESS_ERROR, e);
			if ( processId >0 && processId > 0 ) {
				markFlowAndProcessAsFailed(flowProcessData, flowId);
			}
		} finally {
			threadStopped(threadUUID);
		}
		return "";
	}
	
	private void markFlowAndProcessAsSuccess(Long flowId) {
		if ( flowId > 0 ) {
			FlowInputData flowInputData =  HQLHelper.loadFlowInputDataEntity(flowInputDataRepository, flowId);
			if ( flowInputData != null ) {
				flowInputData.setSinked(false);
				flowInputData.setProcessed(true);
				flowInputData.setClosed(false);
				HQLHelper.saveAndRetryFlowInputDataEntity(flowInputDataRepository, flowInputData, vlfLogger);
			}
		}
	}
	
	private void markFlowAndProcessAsFailed(FlowProcessData flowProcessData, Long flowId) {
		if ( flowId > 0 ) {
			FlowInputData flowInputData =  HQLHelper.loadFlowInputDataEntity(flowInputDataRepository, flowId);
			if ( flowInputData != null ) {
				flowInputData.setSinked(false);
				flowInputData.setProcessed(false);
				flowInputData.setClosed(true);
				HQLHelper.saveAndRetryFlowInputDataEntity(flowInputDataRepository, flowInputData, vlfLogger);
			}
		}
		if ( flowProcessData != null ) {
			flowProcessData.setClosed(true);
			HQLHelper.saveAndRetryFlowProcessDataEntity(flowProcessDataRepository, flowProcessData, vlfLogger);
		}
	}
}
