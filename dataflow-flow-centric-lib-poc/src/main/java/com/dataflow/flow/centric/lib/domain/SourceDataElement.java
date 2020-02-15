/**
 * 
 */
package com.dataflow.flow.centric.lib.domain;

import java.io.Serializable;

import org.bson.BsonDocument;

/**
 * @author Administrator
 *
 */
public class SourceDataElement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8202502900267205140L;
	
	private Long flowId;
	private String modelType;
	private BsonDocument bsonObject;
	/**
	 * @param flowId
	 * @param modelType
	 * @param bsonObject
	 */
	public SourceDataElement(Long flowId, String modelType, BsonDocument bsonObject) {
		super();
		this.flowId = flowId;
		this.modelType = modelType;
		this.bsonObject = bsonObject;
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
	 * @return the bsonObject
	 */
	public BsonDocument getBsonObject() {
		return bsonObject;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bsonObject == null) ? 0 : bsonObject.hashCode());
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
		if (bsonObject == null) {
			if (other.bsonObject != null)
				return false;
		} else if (!bsonObject.equals(other.bsonObject))
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
		return "SourceDataElement [flowId=" + flowId + ", modelType=" + modelType + ", bsonObject=" + (bsonObject != null ? bsonObject.toJson() : "null") + "]";
	}
	
}
