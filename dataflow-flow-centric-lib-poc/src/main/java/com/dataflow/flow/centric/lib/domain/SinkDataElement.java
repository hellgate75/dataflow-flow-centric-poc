/**
 * 
 */
package com.dataflow.flow.centric.lib.domain;

import java.io.Serializable;
import java.util.Optional;

import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.BsonString;
import org.bson.BsonType;
import org.bson.BsonValue;

import com.dataflow.flow.centric.lib.helper.GenericHelper;
import com.dataflow.flow.centric.lib.helper.JMSHelper;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class SinkDataElement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1189407620051989008L;
	private Long flowId;
	private Long processId;
	private String modelType;
	private String jsonString;
	private String index;
	private String bsonMetadataId;
	private String bsonMetadataCollection;
	private String noSqlCollection;
	private String mongoDocumentId;
	/**
	 * 
	 */
	public SinkDataElement() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param flowId
	 * @param processId
	 * @param modelType
	 * @param bsonInputObjectWithmetadata
	 * @param bsonMetadataId
	 * @param noSqlCollection
	 * @param mongoDocumentId
	 */
	public SinkDataElement(Long flowId, Long processId, String modelType, BsonDocument bsonInputObjectWithmetadata,
			String index, String bsonMetadataId, String bsonMetadataCollection, String noSqlCollection, String mongoDocumentId) {
		super();
		this.flowId = flowId;
		this.processId = processId;
		this.modelType = modelType;
		this.jsonString = JMSHelper.escapeJsonString(bsonInputObjectWithmetadata.toJson());
		this.index=index;
		this.bsonMetadataId = bsonMetadataId;
		this.noSqlCollection = noSqlCollection;
		this.mongoDocumentId = mongoDocumentId;
		this.bsonMetadataCollection = bsonMetadataCollection;
	}
	/**
	 * @param flowId
	 * @param processId
	 * @param modelType
	 * @param bsonInputObjectWithmetadata
	 * @param bsonMetadataId
	 * @param noSqlCollection
	 * @param mongoDocumentId
	 */
	protected SinkDataElement(Long flowId, Long processId, String modelType, String bsonObject, String index, 
			String bsonMetadataId, String bsonMetadataCollection, String noSqlCollection, String mongoDocumentId) {
		super();
		this.flowId = flowId;
		this.processId = processId;
		this.modelType = modelType;
		this.jsonString = JMSHelper.isJsonStringEscapee(bsonObject) ? bsonObject : JMSHelper.escapeJsonString(bsonObject);
		this.index=index;
		this.bsonMetadataId = bsonMetadataId;
		this.noSqlCollection = noSqlCollection;
		this.mongoDocumentId = mongoDocumentId;
		this.bsonMetadataCollection = bsonMetadataCollection;
	}
	/**
	 * @return the flowId
	 */
	public Long getFlowId() {
		return flowId;
	}
	/**
	 * @return the processId
	 */
	public Long getProcessId() {
		return processId;
	}
	/**
	 * @return the modelType
	 */
	public String getModelType() {
		return modelType;
	}
	/**
	 * @return the bsonInputObjectWithmetadata
	 */
	public BsonDocument getBsonInputObjectWithmetadata() {
		return BsonDocument.parse(JMSHelper.unescapeJsonString(jsonString));
	}
	/**
	 * @return the bsonMetadataId
	 */
	public String getBsonMetadataId() {
		return bsonMetadataId;
	}
	/**
	 * @return the bsonMetadataId
	 */
	public String getBsonMetadataCollection() {
		return bsonMetadataCollection;
	}
	/**
	 * @return the noSqlCollection
	 */
	public String getNoSqlCollection() {
		return noSqlCollection;
	}
	/**
	 * @return the mongoDocumentId
	 */
	public String getMongoDocumentId() {
		return mongoDocumentId;
	}
	/**
	 * @param flowId the flowId to set
	 */
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	/**
	 * @param processId the processId to set
	 */
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	/**
	 * @param modelType the modelType to set
	 */
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	/**
	 * @param bsonMetadataId the bsonMetadataId to set
	 */
	public void setBsonMetadataId(String bsonMetadataId) {
		this.bsonMetadataId = bsonMetadataId;
	}
	/**
	 * @param bsonMetadataCollection the bsonMetadataCollection to set
	 */
	public void setBsonMetadataCollection(String bsonMetadataCollection) {
		this.bsonMetadataCollection = bsonMetadataCollection;
	}
	/**
	 * @param noSqlCollection the noSqlCollection to set
	 */
	public void setNoSqlCollection(String noSqlCollection) {
		this.noSqlCollection = noSqlCollection;
	}
	/**
	 * @param mongoDocumentId the mongoDocumentId to set
	 */
	public void setMongoDocumentId(String mongoDocumentId) {
		this.mongoDocumentId = mongoDocumentId;
	}
	/**
	 * @return the jsonString
	 */
	public String getJsonString() {
		if ( JMSHelper.isJsonStringEscapee(jsonString) )
			return JMSHelper.unescapeJsonString(jsonString);
		return jsonString;
	}
	/**
	 * @param jsonString the jsonString to set
	 */
	public void setJsonString(String jsonString) {
		if ( ! JMSHelper.isJsonStringEscapee(jsonString) )
			jsonString = JMSHelper.escapeJsonString(jsonString);
		this.jsonString = jsonString;
	}
	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jsonString == null) ? 0 : jsonString.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
		result = prime * result + ((bsonMetadataId == null) ? 0 : bsonMetadataId.hashCode());
		result = prime * result + ((flowId == null) ? 0 : flowId.hashCode());
		result = prime * result + ((modelType == null) ? 0 : modelType.hashCode());
		result = prime * result + ((mongoDocumentId == null) ? 0 : mongoDocumentId.hashCode());
		result = prime * result + ((noSqlCollection == null) ? 0 : noSqlCollection.hashCode());
		result = prime * result + ((bsonMetadataCollection == null) ? 0 : bsonMetadataCollection.hashCode());
		result = prime * result + ((processId == null) ? 0 : processId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SinkDataElement other = (SinkDataElement) obj;
		if (jsonString == null) {
			if (other.jsonString != null)
				return false;
		} else if (!jsonString.equals(other.jsonString))
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		if (bsonMetadataId == null) {
			if (other.bsonMetadataId != null)
				return false;
		} else if (!bsonMetadataId.equals(other.bsonMetadataId))
			return false;
		if (flowId == null) {
			if (other.flowId != null)
				return false;
		} else if (!flowId.equals(other.flowId))
			return false;
		if (modelType == null) {
			if (other.modelType != null)
				return false;
		} else if (!modelType.equals(other.modelType))
			return false;
		if (mongoDocumentId == null) {
			if (other.mongoDocumentId != null)
				return false;
		} else if (!mongoDocumentId.equals(other.mongoDocumentId))
			return false;
		if (noSqlCollection == null) {
			if (other.noSqlCollection != null)
				return false;
		} else if (!noSqlCollection.equals(other.noSqlCollection))
			return false;
		if (bsonMetadataCollection == null) {
			if (other.bsonMetadataCollection != null)
				return false;
		} else if (!bsonMetadataCollection.equals(other.bsonMetadataCollection))
			return false;
		if (processId == null) {
			if (other.processId != null)
				return false;
		} else if (!processId.equals(other.processId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SinkDataElement [flowId=" + flowId + ", processId=" + processId + ", modelType=" + modelType
				+ ", bsonInputObjectWithmetadata=" + jsonString + ", index=" + index + ", bsonMetadataId=" + bsonMetadataId
				 + ", bsonMetadataCollextion=" + bsonMetadataCollection+ ", noSqlCollection=" + noSqlCollection + ", mongoDocumentId=" + mongoDocumentId + "]";
	}

	/**
	 * @return
	 */
	public String toJson() {
		BsonDocument document = new BsonDocument();
		document.putIfAbsent("flowId", new BsonInt64(flowId));
		document.putIfAbsent("processId", new BsonInt64(processId));
		document.putIfAbsent("modelType", new BsonString(modelType));
		document.putIfAbsent("index", new BsonString(index));
		document.putIfAbsent("jsonString", new BsonString(jsonString));
		document.putIfAbsent("bsonMetadataId", new BsonString(bsonMetadataId));
		document.putIfAbsent("bsonMetadataCollection", new BsonString(bsonMetadataCollection));
		document.putIfAbsent("noSqlCollection", new BsonString(noSqlCollection));
		document.putIfAbsent("mongoDocumentId", new BsonString(mongoDocumentId));
		return document.toJson();
	}
	
	
	/**
	 * @param json
	 * @return
	 */
	public static SinkDataElement fromJson(String json) {
		BsonDocument document = BsonDocument.parse(json);
		String errorMessage = "ProcessedDataElement::fromJson Error related to '%s' of field '%s'";
		if ( ! document.containsKey("flowId") ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "presence", "flowId"));
			
		} 
		Optional<BsonType> flowIdTypeOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("flowId")).map( e -> e.getValue().getBsonType() ).findFirst();
		if ( ! flowIdTypeOpt.isPresent() || (flowIdTypeOpt.get() != BsonType.INT32 &&  flowIdTypeOpt.get() != BsonType.INT64) ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "type check", "flowId"));
		}

		if ( ! document.containsKey("processId") ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "presence", "processId"));
			
		} 
		Optional<BsonType> processIdTypeOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("processId")).map( e -> e.getValue().getBsonType() ).findFirst();
		if ( ! processIdTypeOpt.isPresent() || (processIdTypeOpt.get() != BsonType.INT32 &&  processIdTypeOpt.get() != BsonType.INT64) ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "type check", "processId"));
		}

		if ( ! document.containsKey("mongoDocumentId") ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "presence", "mongoDocumentId"));
			
		} 
		Optional<BsonType> mongoDocumentIdTypeOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("mongoDocumentId")).map( e -> e.getValue().getBsonType() ).findFirst();
		if ( ! mongoDocumentIdTypeOpt.isPresent() || mongoDocumentIdTypeOpt.get() != BsonType.STRING ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "type check", "mongoDocumentId"));
		}
		

		if ( ! document.containsKey("bsonMetadataId") ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "presence", "bsonMetadataId"));
			
		} 
		Optional<BsonType> bsonMetadataIdTypeOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("bsonMetadataId")).map( e -> e.getValue().getBsonType() ).findFirst();
		if ( ! bsonMetadataIdTypeOpt.isPresent() || bsonMetadataIdTypeOpt.get() != BsonType.STRING ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "type check", "bsonMetadataId"));
		}
		
		if ( ! document.containsKey("modelType") ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "presence", "modelType"));
		}

		Optional<BsonType> modelTypeTypeOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("modelType")).map( e -> e.getValue().getBsonType() ).findFirst();
		
		if ( ! modelTypeTypeOpt.isPresent() || modelTypeTypeOpt.get() != BsonType.STRING ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "type check", "modelType"));
		}
		
		if ( ! document.containsKey("index") ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "presence", "index"));
		}

		Optional<BsonType> indexTypeOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("index")).map( e -> e.getValue().getBsonType() ).findFirst();
		
		if ( ! indexTypeOpt.isPresent() || indexTypeOpt.get() != BsonType.STRING ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "type check", "index"));
		}

		if ( ! document.containsKey("bsonMetadataCollection") ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "presence", "bsonMetadataCollection"));
		}

		Optional<BsonType> bsonMetadataCollectionTypeOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("bsonMetadataCollection")).map( e -> e.getValue().getBsonType() ).findFirst();
		
		if ( ! bsonMetadataCollectionTypeOpt.isPresent() || bsonMetadataCollectionTypeOpt.get() != BsonType.STRING ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "type check", "bsonMetadataCollection"));
		}
		
		if ( ! document.containsKey("noSqlCollection") ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "presence", "noSqlCollection"));
		}

		Optional<BsonType> noSqlCollectionTypeOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("noSqlCollection")).map( e -> e.getValue().getBsonType() ).findFirst();
		
		if ( ! noSqlCollectionTypeOpt.isPresent() || noSqlCollectionTypeOpt.get() != BsonType.STRING ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "type check", "noSqlCollection"));
		}
		
		if ( ! document.containsKey("jsonString") ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "presence", "jsonString"));
		}

		Optional<BsonType> jsonStringTypeOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("jsonString")).map( e -> e.getValue().getBsonType() ).findFirst();
		
		if ( ! jsonStringTypeOpt.isPresent() || jsonStringTypeOpt.get() != BsonType.STRING ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "type check", "jsonString"));
		}
		
		Optional<BsonValue> flowIdValueOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("flowId")).map( e -> e.getValue() ).findFirst();
		Optional<BsonValue> processIdValueOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("processId")).map( e -> e.getValue() ).findFirst();
		Optional<BsonValue> bsonMetadataIdValueOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("bsonMetadataId")).map( e -> e.getValue() ).findFirst();
		Optional<BsonValue> mongoDocumentIdValueOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("mongoDocumentId")).map( e -> e.getValue() ).findFirst();
		Optional<BsonValue> modelTypeValueOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("modelType")).map( e -> e.getValue() ).findFirst();
		Optional<BsonValue> indexValueOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("index")).map( e -> e.getValue() ).findFirst();
		Optional<BsonValue> bsonMetadataCollectionValueOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("bsonMetadataCollection")).map( e -> e.getValue() ).findFirst();
		Optional<BsonValue> noSqlCollectionValueOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("noSqlCollection")).map( e -> e.getValue() ).findFirst();
		Optional<BsonValue> jsonStringValueOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("jsonString")).map( e -> e.getValue() ).findFirst();
				
		if ( ! flowIdValueOpt.isPresent() ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "value presence", "flowId"));
		}
		if ( ! processIdValueOpt.isPresent() ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "value presence", "processId"));
		}
		if ( ! bsonMetadataIdValueOpt.isPresent() ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "value presence", "bsonMetadataId"));
		}
		if ( ! mongoDocumentIdValueOpt.isPresent() ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "value presence", "mongoDocumentId"));
		}
		if ( ! modelTypeValueOpt.isPresent() ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "value presence", "modelType"));
		}
		if ( ! indexValueOpt.isPresent() ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "value presence", "index"));
		}
		if ( ! bsonMetadataCollectionValueOpt.isPresent() ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "value presence", "bsonMetadataCollection"));
		}
		if ( ! noSqlCollectionValueOpt.isPresent() ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "value presence", "noSqlCollection"));
		}
		if ( ! jsonStringValueOpt.isPresent() ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "value presence", "jsonString"));
		}

		BsonValue flowIdValue = flowIdValueOpt.get();
		BsonValue processIdValue = processIdValueOpt.get();
		BsonValue bsonMetadataIdValue = bsonMetadataIdValueOpt.get();
		BsonValue mongoDocumentIdValue = mongoDocumentIdValueOpt.get();
		BsonValue modelTypeValue = modelTypeValueOpt.get();
		BsonValue indexValue = indexValueOpt.get();
		BsonValue bsonMetadataCollectionValue = bsonMetadataCollectionValueOpt.get();
		BsonValue noSqlCollectionValue = noSqlCollectionValueOpt.get();
		BsonValue jsonStringValue = jsonStringValueOpt.get();
		Long flowId = flowIdValue.isNull() ? null : flowIdTypeOpt.get() == BsonType.INT32 ? flowIdValue.asInt32().longValue() : flowIdValue.asInt64().getValue();
		Long processId = processIdValue.isNull() ? null : processIdTypeOpt.get() == BsonType.INT32 ? processIdValue.asInt32().longValue() : processIdValue.asInt64().getValue();
		return new SinkDataElement(
					flowId, 
					processId, 
					modelTypeValue.isNull() ? "" : modelTypeValue.asString().getValue(), 
					jsonStringValue.isNull() ? "" : jsonStringValue.asString().getValue(),
					indexValue.isNull() ? "" : indexValue.asString().getValue(), 
					bsonMetadataIdValue.isNull() ? "" : bsonMetadataIdValue.asString().getValue(), 
					bsonMetadataCollectionValue.isNull() ? "" : bsonMetadataCollectionValue.asString().getValue(),
					noSqlCollectionValue.isNull() ? "" : noSqlCollectionValue.asString().getValue(),
					mongoDocumentIdValue.isNull() ? "" : mongoDocumentIdValue.asString().getValue()
				);
	}

}
