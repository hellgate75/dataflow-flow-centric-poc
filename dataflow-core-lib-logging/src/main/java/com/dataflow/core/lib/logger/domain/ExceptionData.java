package com.dataflow.core.lib.logger.domain;

public class ExceptionData {
	
	private String message;
	private String exceptionType;
	private String causedBy = "";
	private String stackTrace;
	private String appMessage;
	/**
	 * @param message
	 * @param exceptionType
	 * @param causedBy
	 * @param stackTrace
	 */
	public ExceptionData(String message, String exceptionType, String causedBy, String stackTrace, String appMessage) {
		this.message = message;
		this.exceptionType = exceptionType;
		this.causedBy = causedBy;
		this.stackTrace = stackTrace;
		this.appMessage = appMessage;
	}
	
	public ExceptionData() {
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the exceptionType
	 */
	public String getExceptionType() {
		return exceptionType;
	}

	/**
	 * @param exceptionType the exceptionType to set
	 */
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	/**
	 * @return the causedBy
	 */
	public String getCausedBy() {
		return causedBy;
	}

	/**
	 * @param causedBy the causedBy to set
	 */
	public void setCausedBy(String causedBy) {
		this.causedBy = causedBy;
	}

	/**
	 * @return the stackTrace
	 */
	public String getStackTrace() {
		return stackTrace;
	}

	/**
	 * @param stackTrace the stackTrace to set
	 */
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	/**
	 * @return the appMessage
	 */
	public String getAppMessage() {
		return appMessage;
	}

	/**
	 * @param appMessage the appMessage to set
	 */
	public void setAppMessage(String appMessage) {
		this.appMessage = appMessage;
	}


	
}
