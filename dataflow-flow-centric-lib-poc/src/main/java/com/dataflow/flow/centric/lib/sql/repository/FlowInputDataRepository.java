package com.dataflow.flow.centric.lib.sql.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.dataflow.flow.centric.lib.constants.HQLConstants;
import com.dataflow.flow.centric.lib.sql.entity.FlowInputData;

public interface FlowInputDataRepository extends CrudRepository<FlowInputData, Long> {

	@Query(HQLConstants.SELECT_ALL_OPEN_FLOW_INPUT)
	public List<FlowInputData> getAllOpenFlowInputData();

	@Query(HQLConstants.SELECT_ALL_CLOSED_FLOW_INPUT)
	public List<FlowInputData> getAllClosedFlowInputData();

	@Query(HQLConstants.SELECT_ONE_FLOW_INPUT)
	public Optional<FlowInputData> selectOne(Long id);
}
