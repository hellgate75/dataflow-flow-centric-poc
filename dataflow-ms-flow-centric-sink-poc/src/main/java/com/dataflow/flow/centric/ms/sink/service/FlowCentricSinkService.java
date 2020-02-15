/**
 * 
 */
package com.dataflow.flow.centric.ms.sink.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.flow.centric.lib.domain.ProcessedDataElement;
import com.dataflow.flow.centric.lib.domain.SinkDataElement;
import com.dataflow.flow.centric.lib.exceptions.IOFlowException;
import com.dataflow.flow.centric.lib.helper.BsonHelper;
import com.dataflow.flow.centric.lib.helper.HQLHelper;
import com.dataflow.flow.centric.lib.service.IFlowCentricService;
import com.dataflow.flow.centric.lib.sql.entity.FlowInputData;
import com.dataflow.flow.centric.lib.sql.entity.FlowProcessData;
import com.dataflow.flow.centric.lib.sql.repository.FlowInputDataRepository;
import com.dataflow.flow.centric.lib.sql.repository.FlowProcessDataRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * @author Administrator
 *
 */
@Component
@EnableScheduling
@Configuration
public class FlowCentricSinkService implements IFlowCentricService<ProcessedDataElement, SinkDataElement> {

    @Value("${spring.data.mongodb.database}")
    private String database;

	@Autowired
	protected VlfLogger vlfLogger;

	@Autowired
	protected FlowInputDataRepository flowInputDataRepository;

	@Autowired
	protected FlowProcessDataRepository flowProcessDataRepository;

	@Autowired
	protected MongoClient mongoClient;
	
	@Autowired
	protected NamesAndStandardsComponent namesAndStandardsComponent;
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
			MongoDatabase db = mongoClient.getDatabase(database);
			collections.addAll(BsonHelper.getMongoDbCollections(db));
			BsonDocument document = inputData.getBsonInputObject();
			String collectionName = namesAndStandardsComponent.normalizeMongoDbCollectionName(inputData.getNoSqlCollection(), document);
			if ( ! collections.contains(collectionName) ) {
				db.createCollection(collectionName);
			}
			MongoCollection<Document> collection = db.getCollection(collectionName);
			document.putIfAbsent("__bason_meta", inputData.getBsonMetadata().asDocument());
			updateSuccess(flowId, flowInputData, flowProcessData);
			Codec<Document> codec = db.getCodecRegistry().get(Document.class);
			Document mongoDocument = codec.decode(document.asBsonReader(), DecoderContext.builder().build());
			collection.insertOne( mongoDocument );
			Long mongoDocumentId = 0l;
			if ( mongoDocument.containsKey("_id") )
				mongoDocumentId = mongoDocument.getLong("_id");
			return new SinkDataElement(flowId, processId, modelType, document, inputData.getBsonMetadata(), collectionName, mongoDocumentId);
		} catch (Exception e) {
			updateFailure(flowId, flowInputData, flowProcessData);
		} finally {
			mongoClient.close();
		}
		return null;
	}
	
}
