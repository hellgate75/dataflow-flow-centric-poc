package com.dataflow.core.lib.logger.domain;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ApplicationLogDetail extends ApplicationLogHeader{
	
	private ZonedDateTime logTime;
	private String operation;
	private Object logData;
	private String logInstanceId = UUID.randomUUID().toString();
	private static final String EMPTY_STRING = "";
	
	/**
	 * @param logId
	 * @param timestamp
	 * @param logType
	 * @param type
	 * @param layout
	 * @param hostName
	 * @param appName
	 * @param serviceName
	 * @param serviceId
	 * @param region
	 * @param zone
	 * @param clientHost
	 * @param userId
	 * @param dataKey1
	 * @param dataKey2
	 * @param dataKey3
	 * @param trackingId
	 * @param callerTrackingId
	 */
	public ApplicationLogDetail(String logId, ZonedDateTime timestamp, String logType, String type, Layout layout,
			String hostName, String appName, String serviceName, String serviceId, String region, String zone,
			String clientHost, String userId, String dataKey1, String dataKey2, String dataKey3, String trackingId,
			String callerTrackingId, ZonedDateTime logTime, String operation, Object logData) {
		this.logId = logId;
		this.timestamp = timestamp;
		this.logType = logType;
		this.type = type;
		this.layout = layout;
		this.hostName = hostName;
		this.appName = appName;
		this.serviceName = serviceName;
		this.serviceId = serviceId;
		this.region = region;
		this.zone = zone;
		this.clientHost = clientHost;
		this.userId = userId;
		this.dataKey1 = dataKey1;
		this.dataKey2 = dataKey2;
		this.dataKey3 = dataKey3;
		this.trackingId = trackingId;
		this.callerTrackingId = callerTrackingId;
		this.logTime = logTime;
		this.operation = operation;
		this.logData = logData;
	}
	public ApplicationLogDetail() {
		logTime = ZonedDateTime.now();
	}
	
	/*
	 * Makes a shallow clone of the parent object.
	 */
	public static ApplicationLogDetail cloneHeader(ApplicationLogHeader header) {
		ZonedDateTime now = ZonedDateTime.now();
		ApplicationLogDetail detail = new ApplicationLogDetail(header.getLogId(), now,
				header.getLogType(),
				header.getType(), Layout.DETAIL, header.getHostName(), header.getAppName(), header.getServiceName(),
				header.getServiceId(), header.getRegion(), header.getZone(),
				header.getClientHost(), header.getUserId(), 
				header.getDataKey1(), header.getDataKey2(), header.getDataKey3(),
				header.getTrackingId(), header.getCallerTrackingId(),
				now, EMPTY_STRING, EMPTY_STRING);
		return detail;
	}
	
	/**
	 * @return the logTime
	 */
	public ZonedDateTime getLogTime() {
		return logTime;
	}
	/**
	 * @param logTime the logTime to set
	 */
	public void setLogTime(ZonedDateTime logTime) {
		this.logTime = logTime;
	}
	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}
	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
	/**
	 * @return the logData
	 */
	public Object getLogData() {
		return logData;
	}
	/**
	 * @param logData the logData to set
	 */
	public void setLogData(Object logData) {
		this.logData = logData;
	}
	/**
	 * @return the logInstanceId
	 */
	public String getLogInstanceId() {
		return logInstanceId;
	}
	/**
	 * @param logInstanceId the logInstanceId to set
	 */
	public void setLogInstanceId(String logInstanceId) {
		this.logInstanceId = logInstanceId;
	}
	
	

}
