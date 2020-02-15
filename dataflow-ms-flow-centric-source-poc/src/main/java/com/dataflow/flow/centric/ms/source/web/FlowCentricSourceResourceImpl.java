package com.dataflow.flow.centric.ms.source.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.core.lib.logger.VlfLogger.Category;
import com.dataflow.core.lib.logger.VlfLogger.LogLevel;
import com.dataflow.core.lib.logger.VlfLogger.Severity;
import com.dataflow.flow.centric.lib.constants.LoggerConstants;
import com.dataflow.flow.centric.lib.helper.GenericHelper;
import com.dataflow.flow.centric.lib.helper.LoggerHelper;
import com.dataflow.flow.centric.lib.session.FlowCentricRequest;
import com.dataflow.flow.centric.lib.session.FlowCentricResponse;
import com.dataflow.flow.centric.lib.session.validation.RequestValidator;
import com.dataflow.flow.centric.ms.source.stream.SourceDataFlowOut;

@Component
@Configuration
@RestController
@RequestMapping("/")
public class FlowCentricSourceResourceImpl implements FlowCentricSourceResource {
	
	@Autowired
	protected RequestValidator requestValidator;

	/**
	 * Inject the logger.
	 */
	@Autowired
	protected VlfLogger vlfLogger;
	
	@Autowired
	protected SourceDataFlowOut sourceDataFlowOut;

	final String[] ALLOWED_FIELDS = new String[] { "flowCentricRequest.clientName",
			"flowCentricRequest.userId", "flowCentricRequest.clientHost", "flowCentricRequest.token", 
			"flowCentricRequest.action", "flowCentricRequest.parameters"};

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAllowedFields(ALLOWED_FIELDS);
	}

	
	
	@Override
	@RequestMapping(value = "/acquire", method = RequestMethod.POST, consumes = { "application/json" }, produces = {
	"application/json" })
	public ResponseEntity<FlowCentricResponse> acquireJson(@RequestBody String jsonObjectText) {
		FlowCentricResponse flowCentricResponse = new FlowCentricResponse(HttpStatus.OK.name(), "200", "SUCCESS");
		boolean error = false;
		try {
			GenericMessage<String> message = new GenericMessage<String>(jsonObjectText);
			sourceDataFlowOut.output().send(message);
		} catch (Exception e) {
			String message = String.format("Unable to execute send data for Stream Data Sourcing -> error (%s) message: %s -> input : %s", e.getClass().getName(), e.getMessage(), jsonObjectText);
			LoggerHelper.logError(vlfLogger, "FlowCentricSourceResourceImpl::acquireJson", message, Category.BUSINESS_ERROR, e);
			flowCentricResponse = new FlowCentricResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), "500", "ERROR");
			flowCentricResponse.setResponseMessage(message);
			error = true;
		}
		
		ResponseEntity<FlowCentricResponse> httpResponse = new ResponseEntity<FlowCentricResponse>(flowCentricResponse,
				error ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK);
		if ( ! error )
			vlfLogger.write(LoggerConstants.SOURCESSVCID, LoggerConstants.LOG_OPERATION_PING, Severity.INFO,
					"Acquire was Ok!!", LogLevel.INFO);
		return httpResponse;
	}



	@Override
	@RequestMapping(value = "/ping", method = RequestMethod.POST, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<FlowCentricResponse> ping(@RequestBody FlowCentricRequest flowCentricRequest,
													HttpServletRequest request) {
		vlfLogger.write(LoggerConstants.SOURCESSVCID, LoggerConstants.LOG_OPERATION_PING, Severity.INFO,
				"Service request: " + flowCentricRequest, LogLevel.INFO);
		FlowCentricResponse flowCentricResponse = new FlowCentricResponse(HttpStatus.OK.name(), "200", "SUCCESS");
		if ( ! requestValidator.validateRequest(flowCentricRequest, flowCentricResponse) ) {
			vlfLogger.write(LoggerConstants.SOURCESSVCID, LoggerConstants.LOG_OPERATION_PING, Severity.ERROR,
					"Error : UNAUTHORIZED", LogLevel.ERROR);
			flowCentricResponse = new FlowCentricResponse(HttpStatus.UNAUTHORIZED.name(), "401", "Unauthorized");
			flowCentricResponse.setResponseMessage("Unauthorized Access to the REST endpoint.");
			return new ResponseEntity<>(flowCentricResponse, HttpStatus.OK);
		}
		try {
			ResponseEntity<FlowCentricResponse> httpResponse = new ResponseEntity<FlowCentricResponse>(flowCentricResponse,
					HttpStatus.OK);
			vlfLogger.write(LoggerConstants.SOURCESSVCID, LoggerConstants.LOG_OPERATION_PING, Severity.INFO,
					"Ping is OK!!", LogLevel.INFO);
			return httpResponse;
		} catch (Exception e) {
			vlfLogger.write(LoggerConstants.SOURCESSVCID, LoggerConstants.LOG_OPERATION_PING, Severity.ERROR,
					"Ping Service - Request generic error -> " + e.getMessage() , LogLevel.ERROR);
			vlfLogger.write(LoggerConstants.SOURCESSVCID, Severity.ERROR, e);
			flowCentricResponse = new FlowCentricResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), "500", "Internal Server Error");
			flowCentricResponse.setResponseMessage(String.format("Error: <%s> stack: %s", e.getMessage(), GenericHelper.convertStackTrace(e.getStackTrace())));
			return new ResponseEntity<>(flowCentricResponse, HttpStatus.OK);
		}
	}

}
