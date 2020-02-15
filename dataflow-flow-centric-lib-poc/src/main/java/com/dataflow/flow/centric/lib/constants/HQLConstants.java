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

	
	
	
	
	public static final String SELECT_OPEN_MERGE_PROCESSES_BY_PROCESS_NAME = 
			"SELECT u FROM MergeProcessEntity u " + 
			"WHERE u.processName=?1 AND u.state != 'COMPLETE' AND u.state != 'FAILED'";

	public static final String SELECT_SPLITTING_MERGE_PROCESSES_BY_PROCESS_NAME = 
			"SELECT u FROM MergeProcessEntity u " + 
			"WHERE u.processName=?1 AND (u.state = 'CREATED' OR u.state = 'SPLIT')";

	public static final String SELECT_INITIALIZING_MERGE_PROCESSES_BY_PROCESS_NAME = 
			"SELECT u FROM MergeProcessEntity u " + 
			"WHERE u.processName=?1 AND (u.state = 'CREATED' OR u.state = 'INITIALIZATION')";

	public static final String SELECT_ALL_MERGE_PROCESSES_BY_PROCESS_NAME = 
			"SELECT u FROM MergeProcessEntity u " +
			"WHERE u.processName=?1";

	public static final String UPDATE_MERGE_PROCESSES_STATE_BY_PROCESS_ID = 
			"UPDATE MergeProcessEntity u " + 
			"SET u.state=?2 " + 
			"WHERE u.id=?1";

	public static final String UPDATE_MERGE_PROCESSES_STATE_BY_PROCESS_NAME = 
			"UPDATE MergeProcessEntity u " + 
			"SET u.state=?2 " + 
			"WHERE u.processName=?1";


	public static final String UPDATE_MERGE_PROCESSES_VISION_RECORDS_AND_NGB_MATCHES_BY_PROCESS_ID = 
			"UPDATE MergeProcessEntity u " + 
			"SET u.visionProcessedRecords=?2, " + 
			"u.ngbMatchedRecords=?3 " + 
			"WHERE u.id=?1";
	
	public static final String UPDATE_MERGE_PROCESSES_VISION_FILE_DATA_BY_PROCESS_ID = 
			"UPDATE MergeProcessEntity u " + 
			"SET u.visionFileIdentifier=?2, u.visionFileSize=?3, u.visionFileNoChunks=?4 " + 
			"WHERE u.id=?1";

	public static final String UPDATE_MERGE_PROCESSES_NGB_FILE_DATA_BY_PROCESS_ID = 
			"UPDATE MergeProcessEntity u " + 
			"SET u.ngbFileIdentifier=?2, u.ngbFileSize=?3, u.ngbFileNoChunks=?4 " + 
			"WHERE u.id=?1";

	public static final String UPDATE_MERGE_PROCESSES_NGB_AND_VISION_MATCHES_BY_PROCESS_ID = 
			"UPDATE MergeProcessEntity u " + 
			"SET u.visionProcessedRecords=?2, u.ngbMatchedRecords=?3 " + 
			"WHERE u.id=?1";

	public static final String SELECT_ALL_REFERENCE_DATA_BY_PROCESS_NAME = 
			"SELECT u FROM ReferenceDataEntity u " + 
			"WHERE u.processName=?1";

	public static final String SELECT_ALL_REFERENCE_DATA_BY_PROCESS_NAME_AND_FILE_TYPE = 
			"SELECT u FROM ReferenceDataEntity u " + 
			"WHERE u.processName=?1 AND u.fileIdentifier=?2";
	
	public static final String SELECT_ALL_MERGE_TASKS_PER_PROCESS_ID = 
			"SELECT u FROM MergeTasksEntity u " +
			"WHERE u.processId=?1";
	
	public static final String SELECT_ALL_MERGE_TASKS_PER_PROCESS_ID_AND_TASK_NAME = 
			"SELECT u FROM MergeTasksEntity u " +
			"WHERE u.processId=?1 " +
			"AND u.taskName=?2";

	public static final String SELECT_ALL_MERGE_TASKS_PER_PROCESS_ID_AND_GROUP_ID = 
			"SELECT u FROM MergeTasksEntity u " +
			"WHERE u.processId=?1 AND u.groupId=?2";
	
	public static final String SELECT_ALL_OPEN_MERGE_TASKS_PER_PROCESS_ID = 
			"SELECT u FROM MergeTasksEntity u " +
			"WHERE u.processId=?1 AND u.state != 'COMPLETE' AND u.state != 'FAILED'";

	public static final String SELECT_ALL_OPEN_MERGE_TASKS_PER_PROCESS_ID_AND_TASK_NAME = 
			"SELECT u FROM MergeTasksEntity u " +
			"WHERE u.processId=?1 AND u.taskName=?2 " +
			"AND u.state != 'COMPLETE' AND u.state != 'FAILED'";

	public static final String SELECT_ALL_OPEN_MERGE_TASKS_GROUP_IDS_PER_PROCESS_ID = 
			"SELECT DISTINCT u.groupId FROM MergeTasksEntity u " +
			"WHERE u.processId=?1 AND u.groupId>0";

	public static final String SELECT_ALL_OPEN_MERGE_TASKS_PER_PROCESS_ID_AND_GROUP_ID = 
			"SELECT u FROM MergeTasksEntity u " +
			"WHERE u.processId=?1 AND u.groupId=?2" +
			"AND u.state != 'COMPLETE' AND u.state != 'FAILED'";

	public static final String SELECT_MERGE_TASKS_PER_PROCESS_ID_AND_TASK_NAME_AND_VISION_SELECTOR = 
			"SELECT u FROM MergeTasksEntity u " +
			"WHERE u.processId=?1 AND u.taskName=?2 AND u.visionSelector=?3";

	public static final String SELECT_MERGE_TASKS_PER_PROCESS_ID_AND_TASK_NAME_AND_NGB_SELECTOR = 
			"SELECT u FROM MergeTasksEntity u " +
			"WHERE u.processId=?1 AND u.taskName=?2 AND u.ngbSelector=?3";

	public static final String UPDATE_MERGE_TASKS_STATE_BY_TASK_ID = 
			"UPDATE MergeTasksEntity u " + 
			"SET u.state=?2 " + 
			"WHERE u.id=?1";

	public static final String UPDATE_MERGE_TASKS_STATE_BY_TASK_ID_AND_GROUP_ID = 
			"UPDATE MergeTasksEntity u " + 
			"SET u.state=?3 " + 
			"WHERE u.id=?1 AND u.groupId=?2";

	public static final String UPDATE_MERGE_TASKS_STATE_BY_PROCESS_ID_AND_GROUP_ID = 
			"UPDATE MergeTasksEntity u " + 
			"SET u.state=?3 " + 
			"WHERE u.processId=?1 AND u.groupId=?2";

	public static final String UPDATE_MERGE_TASKS_STATE_AND_NODENAME_BY_TASK_ID = 
			"UPDATE MergeTasksEntity u " + 
			"SET u.state=?2, u.nodeName=?3 " + 
			"WHERE u.id=?1";

	public static final String UPDATE_MERGE_TASKS_STATE_MATCHED_BY_TASK_ID = 
			"UPDATE MergeTasksEntity u " + 
			"SET u.state=?2, u.noOfMatches=?3 " + 
			"WHERE u.id=?1";


	public static final String SELECT_ALL_NGB_DISCARDED_RECORDS_BY_PROCESS_ID = 
			"SELECT u " +
			"FROM NgbDiscardedRecordsEntity u " + 
			"WHERE u.processId=?1";

	public static final String SELECT_ALL_NGB_DISCARDED_RECORDS_BY_PROCESS_ID_AND_GROUP_ID = 
			"SELECT u " +
			"FROM NgbDiscardedRecordsEntity u " + 
			"WHERE u.processId=?1 AND u.groupId=?2";

	private HQLConstants() {
	}

}
