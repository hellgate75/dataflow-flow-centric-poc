/**
 * 
 */
package com.dataflow.flow.centric.ms.source.service;

import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.core.lib.logger.VlfLogger.Category;
import com.dataflow.flow.centric.lib.domain.SourceDataElement;
import com.dataflow.flow.centric.lib.exceptions.FlowProcessException;
import com.dataflow.flow.centric.lib.exceptions.IOFlowException;
import com.dataflow.flow.centric.lib.helper.GenericHelper;
import com.dataflow.flow.centric.lib.helper.LoggerHelper;
import com.dataflow.flow.centric.lib.service.IFlowCentricService;
import com.dataflow.flow.centric.lib.sql.repository.FlowInputDataRepository;
import com.dataflow.flow.centric.ms.source.stream.FlowCentricSource;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Component
public class FlowCentricSourceService implements IFlowCentricService<String, SourceDataElement> {
	
	@Autowired
	protected FlowCentricSource flowCentricSource;

	@Autowired
	protected VlfLogger vlfLogger;

	@Autowired
	protected FlowInputDataRepository flowInputDataRepository;
	
	
	
	@Override
	public SourceDataElement computeStreamData(Long flowId, String modelType, String inputData, Object... arguments)
			throws IOFlowException {
		try {
			BsonDocument bsonObject = GenericHelper.jsonTextToBSON(inputData);
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
	

}
