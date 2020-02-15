package com.dataflow.flow.centric.lib.constants;

/**
 * @author VDP Development Constants class containing constants for
 *         data-persistence library
 */
public class GlobalConstants {

	public static final String SERVICE_NAME = "dataflow-MergeLayerPocService";
	public static final String SERVICE_ID = "ML";
	public static final String LOG_OPERATION = "LoadFile";
	public static final String LOG_TYPE_LISTENER = "LISTENER";
	public static final String LOG_TYPE_REQUEST = "REQUEST";
	public static final String LOG_TYPE_RESPONSE = "RESPONSE";
	public static final String LOG_TYPE_SERVICE_LAYER = "SERVICE";
	public static final String LOG_TYPE_QUEUE_LISTENER = "QUEUE_LISTENER";
	public static final String LOG_TYPE_DAO_LAYER = "DAO";
	public static final String EXCEPTION = "EXCEPTION";
	public static final String LOG_TYPE_EVENT = "EVENT";

	public static final String LOG_OPERATION_DIRECTORY_WATCHER = "WATCHDIRECTORY";
	public static final String LOG_OPERATION_DIRECTORY_RELOAD = "LOADFROMDIRECTORY";
	public static final String LOG_OPERATION_PROCESS_LISTENER = "PROCESSLISTEN";
	public static final String LOG_OPERATION_SINK_LISTENER = "SINKLISTEN";
	public static final String CLIENT_NAME = "BILLPROD_WATCHDIRECTORYLISTENER";

	public static final String UNDERSCORE = "_";

	public static final String INITILIZE = "Initialize";

	public static final String LISTEN = "LISTEN";
	public static final String ONMESSAGE = "ONMESSAGE";
	public static final String TRANSFORM = "TRANSFORM";
	public static final String PERSIST_DATA_BMTN = "PERSIST_DATA_BASE_MTN";
	public static final String PERSIST_DATA_BRAW = "PERSIST_DATA_BASE_RAW";
	public static final String PERSIST_DATA_INVOICE = "PERSIST_DATA_INVOICE";
	public static final String LOADFILE = "LOADFILE";

	public static final String HEADER_RECORD = "HEADER_RECORD";
	public static final String TRAILER_RECORD = "TRAILER_RECORD";
	public static final String BODY_RECORD = "BODY_RECORD";
	public static final String LOG_BACKGROUND_THREAD = "BACKGROUND_THREAD";
	public static final String LOG_CALLABLE_THREAD = "CALLABLE_THREAD";
	public static final String SERVICE_MERGELAYER = "MERGELAYER";
	public static final String SERVICE_ID_ALLSERVICES = "ALLSERVICES";

	public static final int THREAD_COUNT = 10;

	private GlobalConstants() {
		// No argument Constructor - Should never be invoked!
	}
}
