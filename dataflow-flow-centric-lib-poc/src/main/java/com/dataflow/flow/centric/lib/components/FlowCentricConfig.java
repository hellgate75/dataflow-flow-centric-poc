/**
 * 
 */
package com.dataflow.flow.centric.lib.components;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.flow.centric.lib.helper.HQLHelper;
import com.dataflow.flow.centric.lib.sql.entity.FlowInputData;
import com.dataflow.flow.centric.lib.sql.entity.FlowProcessData;
import com.dataflow.flow.centric.lib.sql.repository.FlowInputDataRepository;
import com.dataflow.flow.centric.lib.sql.repository.FlowProcessDataRepository;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Component
public class FlowCentricConfig {
	
	@Autowired
	protected VlfLogger vlfLogger; 

	/**
	 * Default Constructor
	 */
	public FlowCentricConfig() {
		super();
	}
	
	@PostConstruct
	protected void loadConfiguration() {
	}
	
	
	/**
	 * @param inputText
	 * @param type
	 * @return
	 */
	public FlowInputData createAndSaveNewFlowInputData(FlowInputDataRepository flowInputDataRepository, String inputText, String type) {
		FlowInputData flowInputData = new FlowInputData(null, type, false, false);
		flowInputData.setInputText(HQLHelper.newClobFromText(inputText));
		return HQLHelper.saveAndRetryFlowInputDataEntity(flowInputDataRepository, flowInputData, vlfLogger);
	}
	
	/**
	 * @param inputText
	 * @param type
	 * @return
	 */
	public FlowProcessData createAndSaveNewFlowProcessData(FlowProcessDataRepository flowProcessDataRepository, FlowInputData inputData, String collection, String metadata) {
		FlowProcessData flowProcessData = new FlowProcessData(null, inputData.getId(), collection);
		flowProcessData.setInputText(HQLHelper.newClobFromText(metadata));
		return HQLHelper.saveAndRetryFlowProcessDataEntity(flowProcessDataRepository, flowProcessData, vlfLogger);
	}
	
	/**
	 * @param flowId
	 * @return
	 */
	public FlowInputData getFlowInputDataById(FlowInputDataRepository flowInputDataRepository, Long flowId) {
		return HQLHelper.loadFlowInputDataEntity(flowInputDataRepository, flowId);
	}
	
}
