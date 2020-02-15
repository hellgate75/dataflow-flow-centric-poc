package com.dataflow.flow.centric.lib.sql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.dataflow.flow.centric.lib.constants.HQLConstants;
import com.dataflow.flow.centric.lib.sql.entity.FlowProcessData;

public interface FlowProcessDataRepository extends CrudRepository<FlowProcessData, Long> {

	@Query(HQLConstants.SELECT_ALL_OPEN_FLOW_PROCESS)
	public List<FlowProcessData> getAllOpenFlowProcessData();

	@Query(HQLConstants.SELECT_ALL_CLOSED_FLOW_PROCESS)
	public List<FlowProcessData> getAllClosedFlowProcessData();

}
