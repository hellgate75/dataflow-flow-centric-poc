/**
 * 
 */
package com.dataflow.flow.centric.lib.session.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dataflow.core.lib.logger.VlfLogger.Status;
import com.dataflow.flow.centric.lib.session.FlowCentricRequest;
import com.dataflow.flow.centric.lib.session.FlowCentricResponse;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Component
public class RequestValidator {

	@Value("${dataflow.app.flow.centric.security.token}")
	protected String securityToken;
	
	public boolean validateRequest(FlowCentricRequest flowCentricRequest, FlowCentricResponse flowCentricResponse) {
		boolean rc = true;

		if (securityToken == null || securityToken.isEmpty() ) {
			flowCentricResponse.setStatus(Status.ERROR.name());
			flowCentricResponse.setResponseMessage("TOKEN config missing");
			flowCentricResponse.setStatusCode("401");
			rc = false;
		} else {
			if (!securityToken.equals(flowCentricRequest.getSecurityToken())) {
				flowCentricResponse.setStatus(Status.ERROR.name());
				flowCentricResponse.setResponseMessage("NOT AUTHORIZED");
				flowCentricResponse.setStatusCode("401");
				rc = false;
			}
		}
		

		return rc;
	}
}
