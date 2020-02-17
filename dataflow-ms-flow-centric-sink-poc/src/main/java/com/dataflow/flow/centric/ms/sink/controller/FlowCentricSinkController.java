/**
 * 
 */
package com.dataflow.flow.centric.ms.sink.controller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.core.lib.logger.VlfLogger.Category;
import com.dataflow.core.lib.logger.VlfLogger.Status;
import com.dataflow.flow.centric.lib.components.FlowCentricConfig;
import com.dataflow.flow.centric.lib.constants.GlobalConstants;
import com.dataflow.flow.centric.lib.domain.ProcessedDataElement;
import com.dataflow.flow.centric.lib.domain.SinkDataElement;
import com.dataflow.flow.centric.lib.helper.LoggerHelper;
import com.dataflow.flow.centric.lib.service.IFlowCentricService;
import com.dataflow.flow.centric.lib.sql.repository.FlowInputDataRepository;
import com.dataflow.flow.centric.lib.sql.repository.FlowProcessDataRepository;
import com.dataflow.flow.centric.lib.stream.domain.ProcessType;
import com.dataflow.flow.centric.lib.stream.listener.IThreadMonitor;
import com.dataflow.flow.centric.ms.sink.stream.FlowCentricSinkOut;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = {FlowInputDataRepository.class, FlowProcessDataRepository.class})
@EnableBinding({Sink.class, FlowCentricSinkOut.class})
public class FlowCentricSinkController implements IThreadMonitor {

	@Value("${dataflow.flow.centric.enable.external.publish}")
	protected boolean publishOnExternalQueue;
	
	public static final Map<ProcessType, ConcurrentHashMap<UUID, String>> THREADS_MAP = new ConcurrentHashMap<>(0);
	
	@Autowired
	protected FlowCentricConfig flowCentricConfig;

	@Autowired
	protected FlowInputDataRepository flowInputDataRepository;
	
	@Autowired
	protected VlfLogger vlfLogger;
	
	@Autowired
	protected IFlowCentricService<ProcessedDataElement, SinkDataElement> flowCentricSinkService;
	
	@Autowired
	protected FlowCentricSinkOut flowCentricSinkOut;
	
	@Value("${dataflow.core.logger.logLevel}")
	private VlfLogger.LogLevel logLevel;
	
	@PostConstruct
	protected void initBean() {
		vlfLogger.newLogSet(GlobalConstants.SERVICE_ID, logLevel);
		this.vlfLogger.write(GlobalConstants.LOG_OPERATION_SINK_LISTENER, GlobalConstants.LOG_TYPE_LISTENER,
				VlfLogger.Severity.INFO, "INIT :: SPRING CLOUD DATA FLOW -> SINK", VlfLogger.LogLevel.INFO);
		for ( ProcessType type: ProcessType.values() ) {
			if ( ! THREADS_MAP.containsKey(type) ) {
				THREADS_MAP.put(type, new ConcurrentHashMap<UUID, String>(0));
			}
		}
	}
	
	@PreDestroy
	protected void destroyBean() {
		this.vlfLogger.write(GlobalConstants.LOG_OPERATION_SINK_LISTENER, GlobalConstants.LOG_TYPE_LISTENER,
				VlfLogger.Severity.INFO, "DESTROY :: SPRING CLOUD DATA FLOW -> SINK", VlfLogger.LogLevel.INFO);
		vlfLogger.endLogSet(Status.SUCCESS, Category.SUCCESS, "200", "DESTROY :: SPRING CLOUD DATA FLOW -> SINK");
	}

	@Override
	public UUID threadStarted(ProcessType processType, String threadName, Long processId, String processName, Integer groupId) {
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

	@ServiceActivator(inputChannel=Sink.INPUT,
			requiresReply = "false",
			autoStartup = "true", 
			async = "true")
	public void sinkFlowCentricAndSend(String inputText) {
		if ( inputText == null || inputText.trim().isEmpty() ) {
			LoggerHelper.logWarning(vlfLogger, "FlowCentricSinkController::sinkFlowCentricAndSend", "Recovered an empty or null message: <" + inputText + ">", null);
			return;
		}
		Long flowId = 0l;
		Long processId = 0l;
		UUID threadUUID = null ;
		try {
			ProcessedDataElement input = ProcessedDataElement.fromJson(inputText);
			if ( input != null ) {
				flowId = input.getFlowId();
				processId = input.getProcessId();
			}
			threadUUID = threadStarted(ProcessType.PROCESS, "FlowCentricSinkProcessedData-"+UUID.randomUUID().toString(), processId, "FlowCentricProcessSourceData-" + UUID.randomUUID().toString(), 0);
			SinkDataElement sde = flowCentricSinkService.computeStreamData(flowId, input.getModelType(), input);
			LoggerHelper.logInfo(vlfLogger, "FlowCentricSinkController::sinkFlowCentricAndSend", "Geenrated Sink data: " + sde);
			
			if ( publishOnExternalQueue && flowCentricSinkOut != null && flowCentricSinkOut.output() != null ) {
				if ( sde != null ) {
					if ( sde.getBsonInputObjectWithmetadata() != null ) {
						try {
							Message<String> message = new GenericMessage<String>(sde.toJson());
							flowCentricSinkOut.output().send(message);
						} catch (Exception e) {
							String errorMessage = String.format("Errors dunring message out of sink with body object: %s",
									"" + sde);
							LoggerHelper.logWarning(vlfLogger, "FlowCentricSinkController::sinkFlowCentricAndSend", errorMessage, e);
						}
					} else {
						LoggerHelper.logWarning(vlfLogger, "FlowCentricSinkController::sinkFlowCentricAndSend", "Null sinked element's internal document , impossible to semd this message, input: " + input, null);
					}
				} else {
					LoggerHelper.logWarning(vlfLogger, "FlowCentricSinkController::sinkFlowCentricAndSend", "Null sinked element, impossoble to semd this message, input: " + input, null);
				}
			} else {
				LoggerHelper.logDebug(vlfLogger, "FlowCentricSinkController::sinkFlowCentricAndSend", "Publish to external domain disabled. Not progressing...");
			}
		} catch (Exception e) {
			String errorMessage = String.format("Error dunring sinking processed data body object body: %s",
					"" + inputText);
			LoggerHelper.logError(vlfLogger, "FlowCentricSinkController::sinkFlowCentricAndSend", errorMessage, Category.BUSINESS_ERROR, e);
		} finally {
			threadStopped(threadUUID);
		}
	}

	
}
