/**
 * 
 */
package com.dataflow.flow.centric.lib.domain;

import org.bson.BsonDocument;

import com.dataflow.flow.centric.lib.domain.metadata.Metadata;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class SinkDataElement {
	private Long flowId;
	private Long processId;
	private String modelType;
	private BsonDocument bsonInputObjectWithmetadata;
	private Metadata bsonMetadata;
	private String noSqlCollection;
	private Long mongoDocumentId;
	/**
	 * @param flowId
	 * @param processId
	 * @param modelType
	 * @param bsonInputObjectWithmetadata
	 * @param bsonMetadata
	 * @param noSqlCollection
	 * @param mongoDocumentId
	 */
	public SinkDataElement(Long flowId, Long processId, String modelType, BsonDocument bsonInputObjectWithmetadata,
			Metadata bsonMetadata, String noSqlCollection, Long mongoDocumentId) {
		super();
		this.flowId = flowId;
		this.processId = processId;
		this.modelType = modelType;
		this.bsonInputObjectWithmetadata = bsonInputObjectWithmetadata;
		this.bsonMetadata = bsonMetadata;
		this.noSqlCollection = noSqlCollection;
		this.mongoDocumentId = mongoDocumentId;
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
		return bsonInputObjectWithmetadata;
	}
	/**
	 * @return the bsonMetadata
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
	/**
	 * @return the mongoDocumentId
	 */
	public Long getMongoDocumentId() {
		return mongoDocumentId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bsonInputObjectWithmetadata == null) ? 0 : bsonInputObjectWithmetadata.hashCode());
		result = prime * result + ((bsonMetadata == null) ? 0 : bsonMetadata.hashCode());
		result = prime * result + ((flowId == null) ? 0 : flowId.hashCode());
		result = prime * result + ((modelType == null) ? 0 : modelType.hashCode());
		result = prime * result + ((mongoDocumentId == null) ? 0 : mongoDocumentId.hashCode());
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
		SinkDataElement other = (SinkDataElement) obj;
		if (bsonInputObjectWithmetadata == null) {
			if (other.bsonInputObjectWithmetadata != null)
				return false;
		} else if (!bsonInputObjectWithmetadata.equals(other.bsonInputObjectWithmetadata))
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
				+ ", bsonInputObjectWithmetadata=" + bsonInputObjectWithmetadata.toJson() + ", bsonMetadata=" + bsonMetadata
				+ ", noSqlCollection=" + noSqlCollection + ", mongoDocumentId=" + mongoDocumentId + "]";
	}

}
