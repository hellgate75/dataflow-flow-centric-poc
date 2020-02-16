/**
 * 
 */
package com.dataflow.flow.centric.lib.domain;

import java.io.Serializable;

import org.bson.BsonDocument;

import com.dataflow.flow.centric.lib.domain.metadata.Metadata;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
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
	private String index;
	private BsonDocument bsonInputObject;
	private Metadata bsonMetadata;
	private String noSqlCollection;

	/**
	 * @param flowId
	 * @param processId
	 * @param modelType
	 * @param index
	 * @param noSqlCollection
	 * @param bsonInputObject
	 * @param bsonMetadataObject
	 */
	public ProcessedDataElement(Long flowId, Long processId, String modelType, String index, String noSqlCollection, BsonDocument bsonInputObject, Metadata bsonMetadataObject) {
		super();
		this.flowId = flowId;
		this.processId = processId;
		this.modelType = modelType;
		this.index = index;
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
	 * @return the index
	 */
	public String getIndex() {
		return index;
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
		result = prime * result + ((bsonInputObject == null) ? 0 : bsonInputObject.hashCode());
		result = prime * result + ((bsonMetadata == null) ? 0 : bsonMetadata.hashCode());
		result = prime * result + ((flowId == null) ? 0 : flowId.hashCode());
		result = prime * result + ((index == null) ? 0 : index.hashCode());
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
		if (bsonInputObject == null) {
			if (other.bsonInputObject != null)
				return false;
		} else if (!bsonInputObject.equals(other.bsonInputObject))
			return false;
		if (bsonMetadata == null) {
			if (other.bsonMetadata != null)
				return false;
		} else if (!bsonMetadata.equals(other.bsonMetadata))
			return false;
		if (flowId == null) {
			if (other.flowId != null)
				return false;
		} else if (!flowId.equals(other.flowId))
			return false;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
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
				+ ", index=" + index + ", bsonInputObject=" + bsonInputObject.toJson() + ", bsonMetadata=" + bsonMetadata
				+ ", noSqlCollection=" + noSqlCollection + "]";
	}

	
}
