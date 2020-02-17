/**
 * 
 */
package com.dataflow.flow.centric.lib.constants;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class HQLConstants {

	public static final String SELECT_ALL_OPEN_FLOW_INPUT = 
			"SELECT u FROM FlowInputData u "+
			"WHERE u.closed = false";

	public static final String SELECT_ALL_CLOSED_FLOW_INPUT = 
			"SELECT u FROM FlowInputData u "+
			"WHERE u.closed = true";

	public static final String SELECT_ALL_OPEN_FLOW_PROCESS = 
			"SELECT u FROM FlowProcessData u "+
			"WHERE u.closed = false";

	public static final String SELECT_ALL_CLOSED_FLOW_PROCESS = 
			"SELECT u FROM FlowProcessData u "+
			"WHERE u.closed = true";

	public static final String SELECT_ONE_FLOW_INPUT = 
			"SELECT u FROM FlowInputData u "+
			"WHERE u.id = ?1";
	
	public static final String SELECT_ONE_FLOW_PROCESS = 
			"SELECT u FROM FlowProcessData u "+
			"WHERE u.id = ?1";
	

	private HQLConstants() {
	}

}
