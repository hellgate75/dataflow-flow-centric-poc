package com.dataflow.core.lib.logger.domain;

import java.time.LocalDateTime;
import java.util.Hashtable;

import com.dataflow.core.lib.logger.VlfLogger.LogLevel;

/**
 * 
 * This class defines instances of the logger data based on thread ID. 
 *
 */
public class TransactionInstanceCache {
	
	private Long threadId;
	private LocalDateTime lastAccessed;
	private ApplicationLogSummary curentLogSet;
	private Hashtable<String, OperationalData> pendingEvents = new Hashtable<String, OperationalData>();
	private LogLevel onetimeLogLevel = LogLevel.ERROR;
	private String indexName;
	
	/**
	 * @param threadId
	 * @param lastAccessed
	 * @param curentLogSet
	 * @param pendingEvents
	 * @param onetimeLogLevel
	 * @param indexName
	 */
	public TransactionInstanceCache(Long threadId, LocalDateTime lastAccessed, ApplicationLogSummary curentLogSet,
			Hashtable<String, OperationalData> pendingEvents, LogLevel onetimeLogLevel, String indexName) {
		this.threadId = threadId;
		this.lastAccessed = lastAccessed;
		this.curentLogSet = curentLogSet;
		this.pendingEvents = pendingEvents;
		this.onetimeLogLevel = onetimeLogLevel;
		this.indexName = indexName;
	}
	
	public TransactionInstanceCache() {
	}

	/**
	 * @return the threadId
	 */
	public Long getThreadId() {
		return threadId;
	}

	/**
	 * @param threadId the threadId to set
	 */
	public void setThreadId(Long threadId) {
		this.threadId = threadId;
	}

	/**
	 * @return the lastAccessed
	 */
	public LocalDateTime getLastAccessed() {
		return lastAccessed;
	}

	/**
	 * @param lastAccessed the lastAccessed to set
	 */
	public void setLastAccessed(LocalDateTime lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	/**
	 * @return the curentLogSet
	 */
	public ApplicationLogSummary getCurentLogSet() {
		return curentLogSet;
	}

	/**
	 * @param curentLogSet the curentLogSet to set
	 */
	public void setCurentLogSet(ApplicationLogSummary curentLogSet) {
		this.curentLogSet = curentLogSet;
	}

	/**
	 * @return the pendingEvents
	 */
	public Hashtable<String, OperationalData> getPendingEvents() {
		return pendingEvents;
	}

	/**
	 * @param pendingEvents the pendingEvents to set
	 */
	public void setPendingEvents(Hashtable<String, OperationalData> pendingEvents) {
		this.pendingEvents = pendingEvents;
	}

	/**
	 * @return the onetimeLogLevel
	 */
	public LogLevel getOnetimeLogLevel() {
		return onetimeLogLevel;
	}

	/**
	 * @param onetimeLogLevel the onetimeLogLevel to set
	 */
	public void setOnetimeLogLevel(LogLevel onetimeLogLevel) {
		this.onetimeLogLevel = onetimeLogLevel;
	}

	/**
	 * @return the indexName
	 */
	public String getIndexName() {
		return indexName;
	}

	/**
	 * @param indexName the indexName to set
	 */
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

}
