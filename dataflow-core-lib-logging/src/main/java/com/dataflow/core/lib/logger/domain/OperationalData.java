package com.dataflow.core.lib.logger.domain;

import java.time.ZonedDateTime;

public class OperationalData {
	private String logId;
	private ZonedDateTime operationTime = ZonedDateTime.now();
	private ZonedDateTime operationCompleteTime = ZonedDateTime.now();
	private String callType;
	private String appName;
	private String appService;
	private String appHost;
	private long responseTime;
	private int requestMsgSize;
	private int replyMsgSize;
	private String status;
	private String category;
	private String errorCode;
	private String statusMsg;
	public OperationalData(){
	}
	public OperationalData(ZonedDateTime operationTime, String callType, String appName, String appService, String appHost,
			int responseTime, int requestMsgSize, int replyMsgSize, String status, String category,
			String errorCode, String statusMsg, String logId) {
		this.operationTime = operationTime;
		this.callType = callType;
		this.appName = appName;
		this.appService = appService;
		this.appHost = appHost;
		this.responseTime = responseTime;
		this.requestMsgSize = requestMsgSize;
		this.replyMsgSize = replyMsgSize;
		this.status = status;
		this.category = category;
		this.errorCode = errorCode;
		this.statusMsg = statusMsg;
		this.logId = logId;
	}
	public ZonedDateTime getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(ZonedDateTime operationTime) {
		this.operationTime = operationTime;
	}
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppService() {
		return appService;
	}
	public void setAppService(String appService) {
		this.appService = appService;
	}
	public String getAppHost() {
		return appHost;
	}
	public void setAppHost(String appHost) {
		this.appHost = appHost;
	}
	public long getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}
	public int getRequestMsgSize() {
		return requestMsgSize;
	}
	public void setRequestMsgSize(int requestMsgSize) {
		this.requestMsgSize = requestMsgSize;
	}
	public int getReplyMsgSize() {
		return replyMsgSize;
	}
	public void setReplyMsgSize(int replyMsgSize) {
		this.replyMsgSize = replyMsgSize;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
	}
	/**
	 * @return the operationCompleteTime
	 */
	public ZonedDateTime getOperationCompleteTime() {
		return operationCompleteTime;
	}
	/**
	 * @param operationCompleteTime the operationCompleteTime to set
	 */
	public void setOperationCompleteTime(ZonedDateTime operationCompleteTime) {
		this.operationCompleteTime = operationCompleteTime;
	}
}
