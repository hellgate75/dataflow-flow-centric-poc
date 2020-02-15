package com.dataflow.flow.centric.ms.sink.web;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.core.lib.logger.VlfLogger.LogLevel;
import com.dataflow.core.lib.logger.VlfLogger.Severity;
import com.dataflow.flow.centric.lib.constants.LoggerConstants;
import com.dataflow.flow.centric.lib.helper.GenericHelper;
import com.dataflow.flow.centric.lib.session.FlowCentricRequest;
import com.dataflow.flow.centric.lib.session.FlowCentricResponse;
import com.dataflow.flow.centric.lib.session.validation.RequestValidator;
import com.dataflow.flow.centric.lib.stream.domain.ProcessType;
import com.dataflow.flow.centric.ms.sink.controller.FlowCentricSinkController;

@Component
@Configuration
@RestController
@RequestMapping("/")
public class MergeLayerSinkResourceImpl implements MergeLayerSinkResource {
	
	@Autowired
	private RequestValidator requestValidator;

	/**
	 * Inject the logger.
	 */
	@Autowired
	private VlfLogger vlfLogger;

	final String[] ALLOWED_FIELDS = new String[] { "mergeLayerRequest.clientName",
			"mergeLayerRequest.userId", "mergeLayerRequest.clientHost", "mergeLayerRequest.token", 
			"mergeLayerRequest.action", "mergeLayerRequest.parameters"};

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAllowedFields(ALLOWED_FIELDS);
	}

	@Override
	@RequestMapping(value = "/ping", method = RequestMethod.POST, consumes = { "application/json" }, produces = {
			"application/json" })
	public ResponseEntity<FlowCentricResponse> ping(@RequestBody FlowCentricRequest mergeLayerRequest,
													HttpServletRequest request) {
		vlfLogger.write(LoggerConstants.SINKSVCID, LoggerConstants.LOG_OPERATION_PING, Severity.INFO,
				"Service request: " + mergeLayerRequest, LogLevel.INFO);
		FlowCentricResponse mergeLayerResponse = new FlowCentricResponse(HttpStatus.OK.name(), "200", "SUCCESS");
		if ( ! requestValidator.validateRequest(mergeLayerRequest, mergeLayerResponse) ) {
			vlfLogger.write(LoggerConstants.SINKSVCID, LoggerConstants.LOG_OPERATION_PING, Severity.ERROR,
					"Error : UNAUTHORIZED", LogLevel.ERROR);
			mergeLayerResponse = new FlowCentricResponse(HttpStatus.UNAUTHORIZED.name(), "401", "Unauthorized");
			mergeLayerResponse.setResponseMessage("Unauthorized Access to the REST endpoint.");
			return new ResponseEntity<>(mergeLayerResponse, HttpStatus.OK);
//			return new ResponseEntity<>(mergeLayerResponse, HttpStatus.UNAUTHORIZED);
		}
		try {
			AtomicInteger counter = new AtomicInteger(0);
			ConcurrentHashMap<String, String> responseMap = new ConcurrentHashMap<>(0);
			FlowCentricSinkController.THREADS_MAP.entrySet()
				.parallelStream().forEach( entry -> {
					ProcessType type = entry.getKey();
					int size = entry.getValue().size();
					responseMap.putIfAbsent("Active " + type.name() + " processes.", "" + size);
					counter.addAndGet(size);
				} );
			mergeLayerResponse.setResponseMap(responseMap);
			mergeLayerResponse.setResponseList(Arrays.asList(new String[] {"Total runnning processes: " + counter.get()}));
			ResponseEntity<FlowCentricResponse> httpResponse = new ResponseEntity<FlowCentricResponse>(mergeLayerResponse,
					HttpStatus.OK);
			vlfLogger.write(LoggerConstants.SINKSVCID, LoggerConstants.LOG_OPERATION_PING, Severity.INFO,
					"Ping is OK!!", LogLevel.INFO);
			return httpResponse;
		} catch (Exception e) {
			vlfLogger.write(LoggerConstants.SINKSVCID, LoggerConstants.LOG_OPERATION_PING, Severity.ERROR,
					"Ping Service - Request generic error -> " + e.getMessage() , LogLevel.ERROR);
			vlfLogger.write(LoggerConstants.SINKSVCID, Severity.ERROR, e);
			mergeLayerResponse = new FlowCentricResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), "500", "Internal Server Error");
			mergeLayerResponse.setResponseMessage(String.format("Error: <%s> stack: %s", e.getMessage(), GenericHelper.convertStackTrace(e.getStackTrace())));
			return new ResponseEntity<>(mergeLayerResponse, HttpStatus.OK);
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
