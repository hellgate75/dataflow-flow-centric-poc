package com.dataflow.core.lib.logger.domain;

import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApplicationLogHeader {
	
	public enum IndexName
	{
		APPLICATIONLOG,
		VLM,
		OFSLL,
		SOLO,
		LOGGING
	};
	
	public enum Layout
	{
		SUMMARY,
		DETAIL
	};
	
	protected String logId;
	@JsonProperty("@timestamp")
	protected ZonedDateTime timestamp;
	protected String logType = Layout.SUMMARY.toString();
	protected String type = "vlf_" +  IndexName.APPLICATIONLOG.toString().toLowerCase() + "_json";
	protected Layout layout = Layout.SUMMARY;
	protected String hostName;
	protected String appName;
	protected String serviceName;
	protected String serviceId;
	protected String region;
	protected String zone;
	protected String clientHost;
	protected String userId;
	protected String dataKey1;
	protected String dataKey2;
	protected String dataKey3;
	protected String trackingId;
	protected String callerTrackingId;

	public ApplicationLogHeader() {
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
	public ApplicationLogHeader(String logId, ZonedDateTime timestamp, String logType, String type, Layout layout,
			String hostName, String appName, String serviceName, String serviceId, String region, String zone,
			String clientHost, String userId, String dataKey1, String dataKey2, String dataKey3, String trackingId,
			String callerTrackingId) {
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
	}



	public ApplicationLogHeader shallowClone()
	{
		ApplicationLogHeader clone = new ApplicationLogHeader(this.logId, this.timestamp, this.logType, this.type, this.layout, 
				this.hostName, this.appName, this.serviceName, this.serviceId, this.region, this.zone,
				this.clientHost, this.userId, this.dataKey1, this.dataKey2, this.dataKey3, this.trackingId,
				this.callerTrackingId);
		return clone;
	}
	
	
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getClientHost() {
		return clientHost;
	}
	public void setClientHost(String clientHost) {
		this.clientHost = clientHost;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(IndexName indexName) {
		this.type = "vlf_" +  indexName.toString().toLowerCase() + "_json";
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	/**
	 * @return the layout
	 */
	public Layout getLayout() {
		return layout;
	}
	/**
	 * @param layout the layout to set
	 */
	public void setLayout(Layout layout) {
		this.layout = layout;
	}
	/**
	 * @return the logType
	 */
	public String getLogType() {
		return logType;
	}
	/**
	 * @param logType the logType to set
	 */
	public void setLogType(String logType) {
		this.logType = logType;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}



	/**
	 * @return the dataKey1
	 */
	public String getDataKey1() {
		return dataKey1;
	}



	/**
	 * @param dataKey1 the dataKey1 to set
	 */
	public void setDataKey1(String dataKey1) {
		this.dataKey1 = dataKey1;
	}



	/**
	 * @return the dataKey2
	 */
	public String getDataKey2() {
		return dataKey2;
	}



	/**
	 * @param dataKey2 the dataKey2 to set
	 */
	public void setDataKey2(String dataKey2) {
		this.dataKey2 = dataKey2;
	}



	/**
	 * @return the dataKey3
	 */
	public String getDataKey3() {
		return dataKey3;
	}



	/**
	 * @param dataKey3 the dataKey3 to set
	 */
	public void setDataKey3(String dataKey3) {
		this.dataKey3 = dataKey3;
	}



	/**
	 * @return the trackingId
	 */
	public String getTrackingId() {
		return trackingId;
	}



	/**
	 * @param trackingId the trackingId to set
	 */
	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}



	/**
	 * @return the callerTrackingId
	 */
	public String getCallerTrackingId() {
		return callerTrackingId;
	}



	/**
	 * @param callerTrackingId the callerTrackingId to set
	 */
	public void setCallerTrackingId(String callerTrackingId) {
		this.callerTrackingId = callerTrackingId;
	}

}
