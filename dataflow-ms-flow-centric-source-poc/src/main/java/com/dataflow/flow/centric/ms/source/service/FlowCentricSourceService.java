/**
 * 
 */
package com.dataflow.flow.centric.ms.source.service;

import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.core.lib.logger.VlfLogger.Category;
import com.dataflow.flow.centric.lib.domain.SourceDataElement;
import com.dataflow.flow.centric.lib.exceptions.FlowProcessException;
import com.dataflow.flow.centric.lib.exceptions.IOFlowException;
import com.dataflow.flow.centric.lib.helper.BsonHelper;
import com.dataflow.flow.centric.lib.helper.LoggerHelper;
import com.dataflow.flow.centric.lib.service.IFlowCentricService;
import com.dataflow.flow.centric.lib.sql.repository.FlowInputDataRepository;
import com.dataflow.flow.centric.ms.source.stream.FlowCentricSource;
import com.dataflow.flow.centric.ms.source.stream.SourceDataFlowOut;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Component
@EnableScheduling
@Configuration
public class FlowCentricSourceService implements IFlowCentricService<String, SourceDataElement> {
	
	@Value("${dataflow.flow.centric.model.field.name}")
	private String bSonModelField;
	
	@Autowired
	protected FlowCentricSource flowCentricSource;

	@Autowired
	protected VlfLogger vlfLogger;

	@Autowired
	protected FlowInputDataRepository flowInputDataRepository;
	
	@Autowired
	protected SourceDataFlowOut sourceDataFlowOut;
	
	
	
	@Override
	public SourceDataElement computeStreamData(Long flowId, String modelType, String inputData, Object... arguments)
			throws IOFlowException {
		try {
			BsonDocument bsonObject = BsonHelper.jsonTextToBSON(inputData);
			// In case of no parse or no exception trigger the call of a null pointer
			// object will cause an exception, else-wise we have a log record in info
			// logging level,
			LoggerHelper.logInfo(vlfLogger, "FlowCentricSourceService::computeStreamData", 
					String.format("BSON Object generated successfullyL %s", bsonObject.toJson()));

			if ( modelType == null && bsonObject != null) {
				if ( arguments.length > 0 ) {
					String bSonModelFieldProcessed = (String) arguments[0];
					if ( bSonModelFieldProcessed!= null && !bSonModelFieldProcessed.isEmpty() && 
							bsonObject.containsKey(bSonModelFieldProcessed) ) {
						modelType = bsonObject.get(bSonModelFieldProcessed).asString().getValue();
					}
					
				}
			}

			LoggerHelper.logInfo(vlfLogger, "FlowCentricSourceService::computeStreamData", 
					String.format("Verify model type %s", modelType.toString()));

			return new SourceDataElement(flowId, modelType, bsonObject);
		} catch (Exception e) {
			String message = String.format("Unable to execute request for Stream Data Processing Service -> error (%s) message: %s -> input : %s", e.getClass().getName(), e.getMessage(), inputData);
			LoggerHelper.logError(vlfLogger, "FlowCentricSourceService::computeStreamData", message, Category.BUSINESS_ERROR, e);
			throw new FlowProcessException(message, e);
		}
	}

	/**
	 * **************** RANDOM DATA GENERATOR FOR PUSHING DATA INTO THE SOURCE STREAM ******************* 
	 * Random JSON Generation for Source Input Stream.
	 * SourceDataFlowOut => output of SourceDataFlowIn input, in this case for a better understanding
	 * SourceDataFlowOut send message to SourceDataFlowIn and then real process step is
	 * SourceDataFlowIn receive Message and activate the Source -> Process -> Sink Data Flow
	 * 
	 */
	@Scheduled(initialDelay = 2000, fixedDelayString = "${dataflow.flow.centric.fake.producer.tick-interval-millis}")
	protected void refreshProcessCommanderServiceList() {
		//TODO: Generate periodically random json and send to Output
		String jsonObjectText = "{\"model\": \"Generic\",\"id\": 5}";
		try {
			GenericMessage<String> message = new GenericMessage<String>(jsonObjectText);
			sourceDataFlowOut.output().send(message);
		} catch (Exception e) {
			String message = String.format("Unable to execute send data for Stream Data Sourcing -> error (%s) message: %s -> input : %s", e.getClass().getName(), e.getMessage(), jsonObjectText);
			LoggerHelper.logError(vlfLogger, "FlowCentricSourceService::refreshProcessCommanderServiceList", message, Category.BUSINESS_ERROR, e);
		}
	}

}
