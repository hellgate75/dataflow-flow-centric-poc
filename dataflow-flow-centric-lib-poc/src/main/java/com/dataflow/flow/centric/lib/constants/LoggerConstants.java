/**
 * 
 */
package com.dataflow.flow.centric.lib.constants;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public final class LoggerConstants {

	public static final String SOURCESSVCID = "SOURCEPOC";
	public static final String PROCESSSVCID = "PROCESSPOC";
	public static final String SINKSVCID = "SINKPOC";
	
	public static final String LOG_OPERATION_SOURCE="SOURCE";
	public static final String LOG_OPERATION_VERIFICATION="VERIFICATION";
	public static final String LOG_OPERATION_PROCESS="PROCESS";
	public static final String LOG_OPERATION_SINK="SINK";
	public static final String LOG_OPERATION_STATISTICS="STATISTICS";
	public static final String LOG_OPERATION_REPORT="REPORT";
	public static final String LOG_OPERATION_WEBHOOK="WEBHOOK";
	public static final String LOG_OPERATION_PING="PING";
	public static final String LOG_OPERATION_JOB_EXECUTION="JOB_EXECUTION";
	
	private LoggerConstants() {
		// Constants must no be instantiated
	}

}
