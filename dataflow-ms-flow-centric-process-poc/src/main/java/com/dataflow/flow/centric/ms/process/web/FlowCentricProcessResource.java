/**
 * 
 */
package com.dataflow.flow.centric.ms.process.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.dataflow.flow.centric.lib.session.FlowCentricRequest;
import com.dataflow.flow.centric.lib.session.FlowCentricResponse;


/**
 * Defines the interface for the Merge Layer Source service.
 *
 */
public interface FlowCentricProcessResource {
	
	/**
	 * @param flowCentricRequest
	 * @param request
	 * @return
	 */
	ResponseEntity<FlowCentricResponse> ping(@RequestBody FlowCentricRequest flowCentricRequest, 
		    HttpServletRequest request);
	

}
