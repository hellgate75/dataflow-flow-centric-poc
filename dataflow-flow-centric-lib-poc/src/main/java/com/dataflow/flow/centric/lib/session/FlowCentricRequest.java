/**
 * 
 */
package com.dataflow.flow.centric.lib.session;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.dataflow.flow.centric.lib.helper.GenericHelper;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class FlowCentricRequest {

	@JsonProperty("clientName")
	private String clientName;
	
	@JsonProperty("userId")
	private String userId;

	@JsonProperty("clientHost")
	private String clientHost;
	
	@JsonProperty("token")
	private String securityToken;

	@JsonProperty("action")
	private String serverAction;
	
	@JsonProperty("parameters")
	private Map<String, String> propertiesMap = new HashMap<>(0);;

	/**
	 * Default Constructor
	 */
	public FlowCentricRequest() {
		super();
	}

	/**
	 * @param securityToken
	 * @param serverAction
	 */
	public FlowCentricRequest(String securityToken, String serverAction) {
		super();
		this.clientName = "FlowCentricSimpleRestClient";
		this.clientHost = GenericHelper.getHostNameString();
		this.userId = GenericHelper.getUserNameString();
		this.securityToken = securityToken;
		this.serverAction = serverAction;
	}

	/**
	 * @param clientName
	 * @param clientHost
	 * @param userId
	 * @param securityToken
	 * @param serverAction
	 */
	public FlowCentricRequest(String clientName, String clientHost, String userId, String securityToken, String serverAction) {
		super();
		this.clientName = clientName;
		this.clientHost = clientHost;
		this.userId = userId;
		this.securityToken = securityToken;
		this.serverAction = serverAction;
	}

	/**
	 * @param securityToken
	 * @param serverAction
	 * @param propertiesMap
	 */
	public FlowCentricRequest(String securityToken, String serverAction, Map<String, String> propertiesMap) {
		this(securityToken, serverAction);
		this.propertiesMap = propertiesMap;
	}

	/**
	 * @param clientName
	 * @param clientHost
	 * @param userId
	 * @param securityToken
	 * @param serverAction
	 * @param propertiesMap
	 */
	public FlowCentricRequest(String clientName, String clientHost, String userId, String securityToken, String serverAction, Map<String, String> propertiesMap) {
		this(clientName, clientHost, userId, securityToken, serverAction);
		this.propertiesMap = propertiesMap;
	}

	/**
	 * @return the securityToken
	 */
	public String getSecurityToken() {
		return securityToken;
	}

	/**
	 * @param securityToken the securityToken to set
	 */
	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

	/**
	 * @return the serverAction
	 */
	public String getServerAction() {
		return serverAction;
	}

	/**
	 * @param serverAction the serverAction to set
	 */
	public void setServerAction(String serverAction) {
		this.serverAction = serverAction;
	}

	/**
	 * @return the propertiesMap
	 */
	@JsonAnyGetter
	public Map<String, String> getPropertiesMap() {
		return propertiesMap;
	}

	/**
	 * @param propertiesMap the propertiesMap to set
	 */
	@JsonAnySetter
	public void setPropertiesMap(Map<String, String> propertiesMap) {
		this.propertiesMap = propertiesMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propertiesMap == null) ? 0 : propertiesMap.hashCode());
		result = prime * result + ((securityToken == null) ? 0 : securityToken.hashCode());
		result = prime * result + ((serverAction == null) ? 0 : serverAction.hashCode());
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
		FlowCentricRequest other = (FlowCentricRequest) obj;
		if (propertiesMap == null) {
			if (other.propertiesMap != null)
				return false;
		} else if (!propertiesMap.equals(other.propertiesMap))
			return false;
		if (securityToken == null) {
			if (other.securityToken != null)
				return false;
		} else if (!securityToken.equals(other.securityToken))
			return false;
		if (serverAction == null) {
			if (other.serverAction != null)
				return false;
		} else if (!serverAction.equals(other.serverAction))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PingRequest [securityToken=" + securityToken + ", serverAction=" + serverAction + ", propertiesMap="
				+ propertiesMap + "]";
	}
	
	
	
}
