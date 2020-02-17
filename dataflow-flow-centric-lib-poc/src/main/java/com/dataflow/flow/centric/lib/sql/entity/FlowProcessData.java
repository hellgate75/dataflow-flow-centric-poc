/**
 * 
 */
package com.dataflow.flow.centric.lib.sql.entity;

import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Entity
@Table(name="TBL_FLOW_PROCESS_DATA")
public class FlowProcessData {
	/*
CREATE TABLE IF NOT EXISTS TBL_FLOW_PROCESS_DATA (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  input_id BIGINT NOT NULL,
  input_text CLOB DEFAULT NULL,
  metadata CLOB DEFAULT NULL,
  collection VARCHAR(64) DEFAULT 'NONE',
  created_ts TIMESTAMP DEFAULT NOW(),
  updated_ts TIMESTAMP DEFAULT NOW()
);	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "input_id", nullable = false)
	private Long inputId;

	@Column(name = "input_text", nullable = true)
	private Clob inputText;
	
	@Column(name = "metadata", nullable = true)
	private Clob metadata;

	@Column(name = "collection", nullable = true)
	private String collectionName;
	
	@Column(name = "closed", nullable = true)
	private Boolean closed;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_ts", nullable = true)
	private Date createdTs;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_ts", nullable = true)
	private Date updatedTs;

	/**
	 * Default Constructor
	 */
	public FlowProcessData() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * DTO entity creation Constructor
	 * @param id
	 * @param inputId
	 * @param inputText
	 * @param metadata
	 * @param collectionName
	 */
	public FlowProcessData(Long id, Long inputId, Clob inputText, Clob metadata, String collectionName) {
		super();
		this.id = id;
		this.inputId = inputId;
		this.inputText = inputText;
		this.metadata = metadata;
		this.collectionName = collectionName;
		this.createdTs = null;
		this.updatedTs = null;
		this.closed = false;
	}


	/**
	 * Partial parameters Constructor
	 * @param id
	 * @param inputId
	 * @param collectionName
	 */
	public FlowProcessData(Long id, Long inputId, String collectionName) {
		this(id, inputId, null, null, collectionName);
	}


	/**
	 * Full Parameters Constructor
	 * @param id
	 * @param inputId
	 * @param inputText
	 * @param metadata
	 * @param collectionName
	 * @param createdTs
	 * @param updatedTs
	 */
	public FlowProcessData(Long id, Long inputId, Clob inputText, Clob metadata, String collectionName, Date createdTs,
			Date updatedTs) {
		this(id, inputId, inputText, metadata, collectionName);
		this.createdTs = createdTs;
		this.updatedTs = updatedTs;
	}


	@PrePersist
	protected void createTimestamp() {
		this.createdTs = new Date(System.currentTimeMillis());
		this.updatedTs = this.createdTs;
	}

	@PreUpdate
	protected void updateTimestamp() {
		this.updatedTs = new Date(System.currentTimeMillis());
	}
	

	/**
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the inputText
	 */
	public Clob getInputText() {
		return inputText;
	}


	/**
	 * @param inputText the inputText to set
	 */
	public void setInputText(Clob inputText) {
		this.inputText = inputText;
	}



	/**
	 * @return the inputId
	 */
	public Long getInputId() {
		return inputId;
	}


	/**
	 * @param inputId the inputId to set
	 */
	public void setInputId(Long inputId) {
		this.inputId = inputId;
	}


	/**
	 * @return the metadata
	 */
	public Clob getMetadata() {
		return metadata;
	}


	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(Clob metadata) {
		this.metadata = metadata;
	}


	/**
	 * @return the collectionName
	 */
	public String getCollectionName() {
		return collectionName;
	}


	/**
	 * @param collectionName the collectionName to set
	 */
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}


	/**
	 * @return the closed
	 */
	public Boolean getClosed() {
		return closed;
	}


	/**
	 * @param closed the closed to set
	 */
	public void setClosed(Boolean closed) {
		this.closed = closed;
	}


	/**
	 * @return
	 */
	public Date getCreatedTs() {
		return createdTs;
	}

	/**
	 * @param createdTs
	 */
	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}

	/**
	 * @return
	 */
	public Date getUpdatedTs() {
		return updatedTs;
	}

	/**
	 * @param updatedTs
	 */
	public void setUpdatedTs(Date updatedTs) {
		this.updatedTs = updatedTs;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inputText == null) ? 0 : inputText.hashCode());
		result = prime * result + ((inputId == null) ? 0 : inputId.hashCode());
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
		FlowProcessData other = (FlowProcessData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inputText == null) {
			if (other.inputText != null)
				return false;
		} else if (!inputText.equals(other.inputText))
			return false;
		if (inputId == null) {
			if (other.inputId != null)
				return false;
		} else if (!inputId.equals(other.inputId))
			return false;
		return true;
	}


	@Override
	public String toString() {
		long len1 = 0;
		try {
			len1 = inputText.length();
		} catch (Exception e) {
		}
		long len2 = 0;
		try {
			len2 = metadata.length();
		} catch (Exception e) {
		}
		return "FlowProcessData [id=" + id + ", inputId=" + inputId + ", inputText lenght=" + len1 + ", metadata lenght="
				+ len2 + ", closed=" + closed + ", collectionName=" + collectionName + ", createdTs=" + createdTs + ", updatedTs="
				+ updatedTs + "]";
	}


	
}
