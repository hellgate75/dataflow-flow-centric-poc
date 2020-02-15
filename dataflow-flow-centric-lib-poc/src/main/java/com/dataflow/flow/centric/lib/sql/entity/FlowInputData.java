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
@Table(name="TBL_FLOW_INPUT_DATA")
public class FlowInputData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "type_name", nullable = false)
	private String typeName;
	
	@Column(name = "input_text", nullable = true)
	private Clob inputText;
	
	@Column(name = "processed")
	private Boolean processed;
	
	@Column(name = "sinked")
	private Boolean sinked;
	
	@Column(name = "closed")
	private Boolean closed;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_ts")
	private Date createdTs;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_ts")
	private Date updatedTs;

	/**
	 * Default Constructor
	 */
	public FlowInputData() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * DTO entity creation Constructor
	 * @param id
	 * @param typeName
	 * @param inputText
	 * @param processed
	 * @param sinked
	 */
	public FlowInputData(Long id, String typeName, Clob inputText, Boolean processed, Boolean sinked) {
		super();
		this.id = id;
		this.typeName = typeName;
		this.inputText = inputText;
		this.processed = processed;
		this.sinked = sinked;
		this.createdTs = null;
		this.updatedTs = null;
	}


	/**
	 * Partial parameters Constructor
	 * @param id
	 * @param typeName
	 * @param processed
	 * @param sinked
	 */
	public FlowInputData(Long id, String typeName, Boolean processed, Boolean sinked) {
		this(id, typeName, null, processed, sinked);
	}
	
	/**
	 * Full Parameters Constructor
	 * @param id
	 * @param typeName
	 * @param inputText
	 * @param processed
	 * @param sinked
	 * @param createdTs
	 * @param updatedTs
	 */
	public FlowInputData(Long id, String typeName, Clob inputText, Boolean processed, Boolean sinked, Date createdTs,
			Date updatedTs) {
		this(id, typeName, inputText, processed, sinked);
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
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}


	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	 * @return the processed
	 */
	public Boolean getProcessed() {
		return processed;
	}


	/**
	 * @param processed the processed to set
	 */
	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}


	/**
	 * @return the sinked
	 */
	public Boolean getSinked() {
		return sinked;
	}


	/**
	 * @param sinked the sinked to set
	 */
	public void setSinked(Boolean sinked) {
		this.sinked = sinked;
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
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
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
		FlowInputData other = (FlowInputData) obj;
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
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}


	@Override
	public String toString() {
		long len = 0;
		try {
			len = inputText.length();
		} catch (Exception e) {
		}
		return "FlowInputData [id=" + id + ", typeName=" + typeName + ", inputText length=" + len + ", processed="
				+ processed + ", sinked=" + sinked + ", closed=" + closed + ", createdTs=" + createdTs + ", updatedTs=" + updatedTs + "]";
	}

	
}
