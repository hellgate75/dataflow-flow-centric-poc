/**
 * 
 */
package com.dataflow.flow.centric.ms.source.controller;

import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.core.lib.logger.VlfLogger.Category;
import com.dataflow.flow.centric.lib.helper.LoggerHelper;
import com.dataflow.flow.centric.ms.source.stream.SourceDataFlowOut;
import com.dataflow.flow.centric.ms.source.utils.JSONGenerationHelper;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Component
@EnableBinding(SourceDataFlowOut.class)
public class DataGenerationSchedulingController {
	
	@Value("${dataflow.flow.centric.model.field.names}")
	private String bSonModelFields;
	
	@Value("${dataflow.flow.centric.model.field.indexes}")
	private String bSonIndexFields;

	@Autowired
	protected SourceDataFlowOut sourceDataFlowOut;

	@Autowired
	protected VlfLogger vlfLogger;

	
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
		BsonDocument bSonDocument = JSONGenerationHelper.generateJSON(bSonModelFields, bSonIndexFields, 3, 3);
		try {
			if ( bSonDocument != null ) {
				GenericMessage<String> message = new GenericMessage<String>(bSonDocument.toJson());
				sourceDataFlowOut.output().send(message);
				LoggerHelper.logInfo(vlfLogger, "DataGenerationSchedulingController::refreshProcessCommanderServiceList", "Created and sent new message");
			} else {
				LoggerHelper.logWarning(vlfLogger, "DataGenerationSchedulingController::refreshProcessCommanderServiceList", "Null message parsed from generator", null);
				
			}
		} catch (Exception e) {
			String message = String.format("Unable to execute send data for Stream Data Sourcing -> error (%s) message: %s -> input : %s", e.getClass().getName(), e.getMessage(), bSonDocument.toString());
			LoggerHelper.logError(vlfLogger, "DataGenerationSchedulingController::refreshProcessCommanderServiceList", message, Category.BUSINESS_ERROR, e);
		}
	}

}
