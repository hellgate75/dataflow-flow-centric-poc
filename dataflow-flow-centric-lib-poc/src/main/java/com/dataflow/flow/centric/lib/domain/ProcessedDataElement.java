/**
 * 
 */
package com.dataflow.flow.centric.lib.domain;

import java.io.Serializable;

import org.bson.BsonDocument;

import com.dataflow.flow.centric.lib.domain.metadata.Metadata;

/**
 * @author Administrator
 *
 */
public class ProcessedDataElement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8202502900267205140L;
	
	private Long flowId;
	private Long processId;
	private String modelType;
	private BsonDocument bsonInputObject;
	private Metadata bsonMetadata;
	private String noSqlCollection;

	/**
	 * @param flowId
	 * @param processId
	 * @param modelType
	 * @param noSqlCollection
	 * @param bsonInputObject
	 * @param bsonMetadataObject
	 */
	public ProcessedDataElement(Long flowId, Long processId, String modelType, String noSqlCollection, BsonDocument bsonInputObject, Metadata bsonMetadataObject) {
		super();
		this.flowId = flowId;
		this.processId = processId;
		this.modelType = modelType;
		this.noSqlCollection = noSqlCollection;
		this.bsonInputObject = bsonInputObject;
		this.bsonMetadata = bsonMetadataObject;
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
	 * @return the bsonInputObject
	 */
	public BsonDocument getBsonInputObject() {
		return bsonInputObject;
	}

	/**
	 * @return the bsonMetadataObject
	 */
	public Metadata getBsonMetadata() {
		return bsonMetadata;
	}

	/**
	 * @return the noSqlCollection
	 */
	public String getNoSqlCollection() {
		return noSqlCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flowId == null) ? 0 : flowId.hashCode());
		result = prime * result + ((modelType == null) ? 0 : modelType.hashCode());
		result = prime * result + ((noSqlCollection == null) ? 0 : noSqlCollection.hashCode());
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
		ProcessedDataElement other = (ProcessedDataElement) obj;
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
		if (noSqlCollection == null) {
			if (other.noSqlCollection != null)
				return false;
		} else if (!noSqlCollection.equals(other.noSqlCollection))
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
		return "ProcessedDataElement [flowId=" + flowId + ", processId=" + processId + ", modelType=" + modelType
				+ ", bsonInputObject=" + (bsonInputObject != null ? bsonInputObject.toJson() : "null") + 
				", bsonMetadataObject=" + (bsonMetadata != null ? bsonMetadata.toJson() : "null")
				+ ", noSqlCollection=" + noSqlCollection + "]";
	}
	
}
