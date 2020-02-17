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
public class SourceDataElement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8202502900267205140L;
	
	private Long flowId;
	private String modelType;
	private String jsonString;

	
	/**
	 * 
	 */
	public SourceDataElement() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param flowId
	 * @param modelType
	 * @param bsonObject
	 */
	public SourceDataElement(Long flowId, String modelType, BsonDocument bsonObject) {
		super();
		this.flowId = flowId;
		this.modelType = modelType;
		this.jsonString = bsonObject!=null ? JMSHelper.escapeJsonString(bsonObject.toJson()) : "";
	}
	/**
	 * @param flowId
	 * @param modelType
	 * @param bsonObject
	 */
	protected SourceDataElement(Long flowId, String modelType, String bsonObject) {
		super();
		this.flowId = flowId;
		this.modelType = modelType;
		this.jsonString =  bsonObject != null && ! bsonObject.trim().isEmpty() ?  ( JMSHelper.isJsonStringEscaped(bsonObject) ? bsonObject : JMSHelper.escapeJsonString(bsonObject)) : "";
	}
	/**
	 * @return the flowId
	 */
	public Long getFlowId() {
		return flowId;
	}
	/**
	 * @return the modelType
	 */
	public String getModelType() {
		return modelType;
	}
	/**
	 * @return the jsonString
	 */
	public String getJsonString() {
		if (jsonString==null || jsonString.trim().isEmpty())
			return "";
		if ( JMSHelper.isJsonStringEscaped(jsonString) )
			return JMSHelper.unescapeJsonString(jsonString);
		return jsonString;
	}
	/**
	 * @param jsonString the jsonString to set
	 */
	public void setJsonString(String jsonString) {
		if (jsonString==null || jsonString.trim().isEmpty())
			return ;
		if ( ! JMSHelper.isJsonStringEscaped(jsonString) )
			jsonString = JMSHelper.escapeJsonString(jsonString);
		this.jsonString = jsonString;
	}
	/**
	 * @param flowId the flowId to set
	 */
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	/**
	 * @param modelType the modelType to set
	 */
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	/**
	 * @return the bsonObject
	 */
	public BsonDocument getBsonObject() {
		if (jsonString==null || jsonString.trim().isEmpty())
			return null;
		return BsonDocument.parse(JMSHelper.unescapeJsonString(jsonString));
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jsonString == null) ? 0 : jsonString.hashCode());
		result = prime * result + ((flowId == null) ? 0 : flowId.hashCode());
		result = prime * result + ((modelType == null) ? 0 : modelType.hashCode());
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
		SourceDataElement other = (SourceDataElement) obj;
		if (jsonString == null) {
			if (other.jsonString != null)
				return false;
		} else if (!jsonString.equals(other.jsonString))
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
		return true;
	}
	@Override
	public String toString() {
		return "SourceDataElement [flowId=" + flowId + ", modelType=" + modelType + ", jsonString=" + (jsonString != null ? jsonString : "<NULL>") + "]";
	}
	
	/**
	 * @return
	 */
	public String toJson() {
		BsonDocument document = new BsonDocument();
		document.putIfAbsent("flowId", new BsonInt64(flowId));
		document.putIfAbsent("modelType", new BsonString(modelType));
		document.putIfAbsent("jsonString", new BsonString(jsonString));
		return document.toJson();
	}
	
	
	/**
	 * @param json
	 * @return
	 */
	public static SourceDataElement fromJson(String json) {
		BsonDocument document = BsonDocument.parse(json);
		String errorMessage = "SourceDataElement::fromJson Error related to '%s' of field '%s'";
		if ( ! document.containsKey("flowId") ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "presence", "flowId"));
			
		} 
		Optional<BsonType> flowIdTypeOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("flowId")).map( e -> e.getValue().getBsonType() ).findFirst();
		
		if ( ! flowIdTypeOpt.isPresent() || (flowIdTypeOpt.get() != BsonType.INT32 &&  flowIdTypeOpt.get() != BsonType.INT64) ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "type check", "flowId"));
		}
		
		if ( ! document.containsKey("modelType") ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "presence", "modelType"));
		}

		Optional<BsonType> modelTypeTypeOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("modelType")).map( e -> e.getValue().getBsonType() ).findFirst();
		
		if ( ! modelTypeTypeOpt.isPresent() || modelTypeTypeOpt.get() != BsonType.STRING ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "type check", "modelType"));
		}
		
		if ( ! document.containsKey("jsonString") ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "presence", "jsonString"));
		}

		Optional<BsonType> jsonStringTypeOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("jsonString")).map( e -> e.getValue().getBsonType() ).findFirst();
		
		if ( ! jsonStringTypeOpt.isPresent() || jsonStringTypeOpt.get() != BsonType.STRING ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "type check", "jsonString"));
		}
		
		Optional<BsonValue> flowIdValueOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("flowId")).map( e -> e.getValue() ).findFirst();
		Optional<BsonValue> modelTypeValueOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("modelType")).map( e -> e.getValue() ).findFirst();
		Optional<BsonValue> jsonStringValueOpt = document.entrySet().parallelStream().filter(e -> e.getKey().equals("jsonString")).map( e -> e.getValue() ).findFirst();

		if ( ! flowIdValueOpt.isPresent() ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "value presence", "flowId"));
		}
		if ( ! modelTypeValueOpt.isPresent() ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "value presence", "modelTyp"));
		}
		if ( ! jsonStringValueOpt.isPresent() ) {
			GenericHelper.thowRuntimeException(String.format(errorMessage, "value presence", "jsonString"));
		}
		
		BsonValue flowIdValue = flowIdValueOpt.get();
		BsonValue modelTypeValue = modelTypeValueOpt.get();
		BsonValue jsonStringValue = jsonStringValueOpt.get();
		Long flowId = flowIdValue.isNull() ? null : flowIdTypeOpt.get() == BsonType.INT32 ? flowIdValue.asInt32().longValue() : flowIdValue.asInt64().getValue();
		return new SourceDataElement(
						flowId, 
						modelTypeValue.isNull() ? "" : modelTypeValue.asString().getValue(), 
						jsonStringValue.isNull() ? "" : jsonStringValue.asString().getValue()
					);
	}

}
