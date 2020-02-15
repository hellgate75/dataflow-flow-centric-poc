/**
 * 
 */
package com.dataflow.flow.centric.lib.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class FlowCentricResponse {

	@JsonProperty("httpStatus")
	private String status;

	@JsonProperty("code")
	private String statusCode;
	
	@JsonProperty("statusMessage")
	private String responseMessage;
	
	@JsonProperty("responseAttributes")
	private Map<String, String> responseMap = new HashMap<>(0);
	
	@JsonProperty("messageList")
	private List<String> responseList = new ArrayList<>(0);

	/**
	 * Default Constructor
	 */
	public FlowCentricResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param status
	 * @param statusCode
	 * @param responseMessage
	 */
	public FlowCentricResponse(String status, String statusCode, String responseMessage) {
		super();
		this.status = status;
		this.statusCode = statusCode;
		this.responseMessage = responseMessage;
	}

	/**
	 * @param status
	 * @param statusCode
	 * @param responseMessage
	 * @param responseMap
	 * @param responseList
	 */
	public FlowCentricResponse(String status, String statusCode, String responseMessage,
			HashMap<String, String> responseMap, List<String> responseList) {
		super();
		this.status = status;
		this.statusCode = statusCode;
		this.responseMessage = responseMessage;
		this.responseMap = responseMap;
		this.responseList = responseList;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	/**
	 * @return the responseMap
	 */
	@JsonAnyGetter
	public Map<String, String> getResponseMap() {
		return responseMap;
	}

	/**
	 * @param responseMap the responseMap to set
	 */
	@JsonAnySetter
	public void setResponseMap(Map<String, String> responseMap) {
		this.responseMap = responseMap;
	}

	/**
	 * @return the responseList
	 */
	public List<String> getResponseList() {
		return responseList;
	}

	/**
	 * @param responseList the responseList to set
	 */
	public void setResponseList(List<String> responseList) {
		this.responseList = responseList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((responseList == null) ? 0 : responseList.hashCode());
		result = prime * result + ((responseMap == null) ? 0 : responseMap.hashCode());
		result = prime * result + ((responseMessage == null) ? 0 : responseMessage.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((statusCode == null) ? 0 : statusCode.hashCode());
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
		FlowCentricResponse other = (FlowCentricResponse) obj;
		if (responseList == null) {
			if (other.responseList != null)
				return false;
		} else if (!responseList.equals(other.responseList))
			return false;
		if (responseMap == null) {
			if (other.responseMap != null)
				return false;
		} else if (!responseMap.equals(other.responseMap))
			return false;
		if (responseMessage == null) {
			if (other.responseMessage != null)
				return false;
		} else if (!responseMessage.equals(other.responseMessage))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (statusCode == null) {
			if (other.statusCode != null)
				return false;
		} else if (!statusCode.equals(other.statusCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MergeLayerResponse [status=" + status + ", statusCode=" + statusCode + ", responseMessage="
				+ responseMessage + ", responseMap=" + responseMap + ", responseList=" + responseList + "]";
	}

	
	
}
