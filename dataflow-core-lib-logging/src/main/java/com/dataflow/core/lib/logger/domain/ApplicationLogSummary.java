package com.dataflow.core.lib.logger.domain;

import java.time.ZonedDateTime;

public class ApplicationLogSummary extends ApplicationLogHeader {
	private OperationalData operationalData = new OperationalData();
	
	public ApplicationLogSummary() {
	}
	
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
	public ApplicationLogSummary(String logId, ZonedDateTime timestamp, String logType, String type, Layout layout,
			String hostName, String appName, String serviceName, String serviceId, String region, String zone,
			String clientHost, String userId, String dataKey1, String dataKey2, String dataKey3, String trackingId,
			String callerTrackingId, OperationalData operationalData) {
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
		this.operationalData = operationalData;
	}
	
	/*
	 * Makes a shallow clone of the parent object.
	 */
	public static ApplicationLogSummary cloneHeader(ApplicationLogHeader header) {
		ApplicationLogSummary log = new ApplicationLogSummary(header.getLogId(), header.getTimestamp(),
				header.getLogType(),
				header.getType(), Layout.DETAIL, header.getHostName(), header.getAppName(), header.getServiceName(),
				header.getServiceId(), header.getRegion(), header.getZone(),
				header.getClientHost(), header.getUserId(), 
				header.getDataKey1(), header.getDataKey2(), header.getDataKey3(),
				header.getTrackingId(), header.getCallerTrackingId(),
				null);
		return log;
	}

	/**
	 * @return the operationalData
	 */
	public OperationalData getOperationalData() {
		return operationalData;
	}

	/**
	 * @param operationalData the operationalData to set
	 */
	public void setOperationalData(OperationalData operationalData) {
		this.operationalData = operationalData;
	}
	
}
