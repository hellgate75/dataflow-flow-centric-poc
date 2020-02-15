/**
 * 
 */
package com.dataflow.flow.centric.ms.source.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.dataflow.flow.centric.lib.session.FlowCentricRequest;
import com.dataflow.flow.centric.lib.session.FlowCentricResponse;


/**
 * Defines the interface for the Merge Layer Source service.
 *
 */
public interface FlowCentricSourceResource {
	
	/**
	 * @param flowCentricRequest
	 * @param request
	 * @return
	 */
	ResponseEntity<FlowCentricResponse> ping(@RequestBody FlowCentricRequest flowCentricRequest, 
		    HttpServletRequest request);
	
	/**
	 * Intercept and process the given JSON text into the Source input stream
	 * @param text
	 * @return
	 */
	ResponseEntity<FlowCentricResponse> acquireJson(@RequestBody String text);
}
