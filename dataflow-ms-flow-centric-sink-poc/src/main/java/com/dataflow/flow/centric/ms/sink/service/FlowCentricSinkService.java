/**
 * 
 */
package com.dataflow.flow.centric.ms.sink.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.core.lib.logger.VlfLogger.Category;
import com.dataflow.flow.centric.lib.domain.ProcessedDataElement;
import com.dataflow.flow.centric.lib.domain.SinkDataElement;
import com.dataflow.flow.centric.lib.exceptions.IOFlowException;
import com.dataflow.flow.centric.lib.helper.HQLHelper;
import com.dataflow.flow.centric.lib.helper.LoggerHelper;
import com.dataflow.flow.centric.lib.service.IFlowCentricService;
import com.dataflow.flow.centric.lib.sink.helper.BsonHelper;
import com.dataflow.flow.centric.lib.sql.entity.FlowInputData;
import com.dataflow.flow.centric.lib.sql.entity.FlowProcessData;
import com.dataflow.flow.centric.lib.sql.repository.FlowInputDataRepository;
import com.dataflow.flow.centric.lib.sql.repository.FlowProcessDataRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Component
public class FlowCentricSinkService implements IFlowCentricService<ProcessedDataElement, SinkDataElement> {

    @Value("${spring.data.mongodb.database}")
    private String database;
    
    @Value("${dataflow.flow.centric.model.disable.exporting.objects}")
    private boolean disableSavingObjects;

	@Autowired
	protected VlfLogger vlfLogger;

	@Autowired
	protected FlowInputDataRepository flowInputDataRepository;

	@Autowired
	protected FlowProcessDataRepository flowProcessDataRepository;

	@Autowired
	protected MongoClient mongoClient;
	
	@Autowired
	protected NamesAndStandardsService namesAndStandardsService;
	/*
	 * Updating the MONITOR DB about success during processing
	 */
	private void updateSuccess(Long flowId, FlowInputData flowInputData, FlowProcessData flowProcessData) {
		long currTime = System.currentTimeMillis();
		if ( flowProcessData != null ) {
			flowProcessData.setClosed(false);
			flowProcessData.setUpdatedTs(new Date(currTime));
			HQLHelper.saveAndRetryFlowProcessDataEntity(flowProcessDataRepository, flowProcessData, vlfLogger);
			if ( flowId == null || flowId <= 0 ) {
				flowId = flowProcessData.getInputId();
			}
		}
		if ( flowInputData != null ) {
			if ( flowId == null || flowId <= 0 ) {
				flowId = flowInputData.getId(); 
			}
		}
		if ( flowId != null && flowId > 0 ) {
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
	private void updateFailure(Long flowId, FlowInputData flowInputData, FlowProcessData flowProcessData) {
		long currTime = System.currentTimeMillis();
		if ( flowProcessData != null ) {
			flowProcessData.setClosed(true);
			flowProcessData.setUpdatedTs(new Date(currTime));
			HQLHelper.saveAndRetryFlowProcessDataEntity(flowProcessDataRepository, flowProcessData, vlfLogger);
			if ( flowId == null || flowId <= 0 ) {
				flowId = flowProcessData.getInputId();
			}
		}
		if ( flowInputData != null ) {
			if ( flowId == null || flowId <= 0 ) {
				flowId = flowInputData.getId(); 
			}
		}
		if ( flowId != null && flowId > 0 ) {
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
	public SinkDataElement computeStreamData(Long streamId, String templateName, ProcessedDataElement inputData,
			Object... arguments) throws IOFlowException {
		Long flowId = null;
		Long processId = null;
		FlowInputData flowInputData = null;
		FlowProcessData flowProcessData = null;
		List<String> collections = new ArrayList<String>(0);
		try {
			processId = inputData.getProcessId();
			flowId = inputData.getFlowId();
			String modelType = inputData.getModelType();
			flowInputData = HQLHelper.loadFlowInputDataEntity(flowInputDataRepository, flowId);
			flowProcessData = HQLHelper.loadFlowProcessDataEntity(flowProcessDataRepository, processId);
			String collectionName = inputData.getNoSqlCollection();
			String mongoDocumentId = "";
			BsonDocument document = null;
			if ( ! disableSavingObjects ) {
				MongoDatabase db = mongoClient.getDatabase(database);
				collections.addAll(BsonHelper.getMongoDbCollections(mongoClient, db));
				document = inputData.getBsonInputObject();
				collectionName = namesAndStandardsService.normalizeMongoDbCollectionName(inputData.getNoSqlCollection(), document);
				if ( ! collections.contains(collectionName) ) {
					BsonHelper.createMongoDbCollection(mongoClient, db, collectionName);
				}
				document.putIfAbsent("__bason_metatada_id", new BsonString(inputData.getBsonMetadataId()));
				document.putIfAbsent("__bason_metatada_collection", new BsonString(inputData.getBsonMetadataCollection()) );
				Codec<Document> codec = db.getCodecRegistry().get(Document.class);
				Document mongoDocument = codec.decode(document.asBsonReader(), DecoderContext.builder().build());
				mongoDocument.put("_object_model", inputData.getModelType());
				mongoDocument.put("_object_index", inputData.getIndex());
				mongoDocument = BsonHelper.saveMongoDbElement(mongoClient, db, collectionName, mongoDocument);
				BsonDocument myDocument = BsonDocument.parse(mongoDocument.toJson());
				Optional<String> mongoObjIdOpt = myDocument.entrySet()
					.parallelStream()
					.filter(e -> e.getKey().contentEquals("_id"))
					.map( e -> {
						BsonValue value = e.getValue();
						if ( value.getBsonType() == BsonType.INT64 ) {
							return ""+value.asInt64().getValue();
						}
						else if ( value.getBsonType() == BsonType.INT32 ) {
							return ""+value.asInt64().longValue();
						}
						else if ( value.getBsonType() == BsonType.STRING ) {
							return value.asString().getValue();
						}
						else if ( value.getBsonType() == BsonType.OBJECT_ID ) {
							return value.asObjectId().getValue().toHexString();
						}
						return null;
					})
					.filter( v -> v != null )
					.findFirst();
				if ( mongoObjIdOpt.isPresent() ) {
					mongoDocumentId = mongoObjIdOpt.get(); 
				}
			}
			updateSuccess(flowId, flowInputData, flowProcessData);
			return new SinkDataElement(flowId, processId, modelType, document, 
					inputData.getIndex(), inputData.getBsonMetadataId(), inputData.getBsonMetadataCollection(), 
					collectionName, mongoDocumentId);
		} catch (Exception e) {
			e.printStackTrace();
			String message = String.format("Unable to execute request for Stream Data Sink Service -> error (%s) message: %s -> input : %s", e.getClass().getName(), e.getMessage(), inputData);
			LoggerHelper.logError(vlfLogger, "FlowCentricSinkService::computeStreamData", message, Category.BUSINESS_ERROR, e);
			updateFailure(flowId, flowInputData, flowProcessData);
		} finally {
			mongoClient.close();
		}
		return null;
	}
	
}
