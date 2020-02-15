package com.dataflow.core.lib.logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;

import com.dataflow.core.lib.logger.domain.ApplicationLogDetail;
import com.dataflow.core.lib.logger.domain.ApplicationLogHeader;
import com.dataflow.core.lib.logger.domain.ApplicationLogSummary;
import com.dataflow.core.lib.logger.domain.ExceptionData;
import com.dataflow.core.lib.logger.domain.OperationalData;
import com.dataflow.core.lib.logger.domain.TransactionInstanceCache;
import com.dataflow.core.lib.logger.domain.ApplicationLogHeader.Layout;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

/**
 * This class creates the standard logging JSON. It allows the application to
 * log both business and operational data.
 *
 */
@Component
public class VlfLogger {

	/**
	 * 
	 * Standard Severity levels
	 *
	 */
	public enum Severity {
		INFO, WARNING, ERROR, FATAL
	}

	/**
	 * 
	 * Standard Status codes
	 *
	 */
	public enum Status {
		SUCCESS, WARNING, ERROR, FATAL
	}

	/**
	 * 
	 * Standard categories
	 *
	 */
	public enum Category {
		SUCCESS, BUSINESS_ERROR, INPUT_DATA_ERROR, SYSTEM_ERROR, TIMEOUT
	}

	/**
	 * 
	 * Logging levels.
	 *
	 */
	public enum LogLevel {
		NONE, FATAL, ERROR, WARN, INFO, VERBOSE, ALL;
	}

	private static final String LINE_SEPARATOR = "line.separator";
	private static final String NA = "N/A";
	private static final String REQUEST = "REQUEST";
	private static final String RESPONSE = "RESPONSE";
	private static final String EVENT = "EVENT";
	private static final String TEXT = "text";
	private static final String EXCEPTION = "EXCEPTION";
	private static final int LOG_BUFFER_INIT_SIZE = 2048;
	private static final String HOST_NAME = "HOSTNAME";
	private static final String COMPUTERNAME = "COMPUTERNAME";
	private static final String ZDTFORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXXXX";
	private static long cachePurgeMinutes = 5;
	@Value("${dataflow.core.logger.appName}")
	private String appName;
	@Value("${dataflow.core.logger.serviceName}")
	private String serviceName;
	private static String hostName = getMachineName();
	@Value("${dataflow.core.logger.zone}")
	private String zone = NA;
	@Value("${dataflow.core.logger.region}")
	private String region = NA;
	@Value("${dataflow.core.logger.batch}")
	private boolean batchMode = false;
	private static ObjectMapper mapper = new ObjectMapper();
	private static Map<Long, TransactionInstanceCache> instanceCache = new ConcurrentHashMap<>();
	private static LocalDateTime cacheCheckTime = LocalDateTime.now().plusMinutes(cachePurgeMinutes);
	@Value("${dataflow.core.logger.indexName}")
	private String indexName = ApplicationLogHeader.IndexName.APPLICATIONLOG.toString().toLowerCase();
	
	private static final Logger logger = LogManager.getLogger(VlfLogger.class);
	private static final Logger systemLogger = LogManager.getRootLogger();
	@Value("${dataflow.core.logger.logLevel}")
	private LogLevel logLevel = LogLevel.ERROR;
	private static TransactionInstanceCache batchInstance = null;
	private static TypeReference<HashMap<String,Object>> mapStrObj = new TypeReference<HashMap<String,Object>>() {};

	/**
	 * Initialize the logging class.
	 * 
	 * @param appName
	 *            The logging application name. This value will be used as the
	 *            primary app name for log searches.
	 * @param serviceName
	 *            The logging application service name. This value will be used
	 *            as the primary service name for log searches.
	 * @param region
	 *            The logging application region.
	 * @param zone
	 *            The logging application zone.
	 * 
	 */
	public VlfLogger(String appName, String serviceName, String region, String zone) {
		this.initializeObjectMapper();
		this.appName = appName;
		this.serviceName = serviceName;
		this.region = region;
		this.zone = zone;
	}
	
	public VlfLogger() {
		this.initializeObjectMapper();
	}
	
	/**
	 * 
	 * @return
	 */
	private static String getMachineNameFromFile() {
		final String constHostName = "/proc/sys/kernel/hostname";
		String hostName = null;
		File f = new File(constHostName);
		if (f.exists() && !f.isDirectory()) {
			try (BufferedReader b = new BufferedReader(new FileReader(f))) {
				hostName = b.readLine();
				if (hostName != null) {
					hostName = hostName.trim();
				}
			} catch (Exception e) {
				systemLogger.error(e.getMessage());
				hostName = null;
			}
		}

		return hostName;
	}
	
	private static String getMachineName() {
		String host = getMachineNameFromFile();
		if(host == null) {
			host = System.getenv(HOST_NAME);
			if(host == null) {
				host = System.getenv(COMPUTERNAME);
				if(host == null) {
					host = NA;
				}
			}
		}
		return host.toUpperCase();
	}

	/**
	 * Initialize the object mapper to format time objects as ISO 8601 date strings when serializing.
	 */
	
	private void initializeObjectMapper() {
		/* Need to add custom serializer as Jackson default is trimming trailing zeros from milliseconds */
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(ZonedDateTime.class,
		  new ZonedDateTimeSerializer(DateTimeFormatter.ofPattern(VlfLogger.ZDTFORMAT)));
		mapper.registerModule(javaTimeModule);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
	
	/**
	 * Initialize a new log set. Use at the beginning of new transactions to
	 * start a new grouping of application logs. Any previously buffered logs
	 * will be written before starting the new log set.
	 * 
	 * @param loggingLevel
	 *            The logging level.
	 */
	public void newLogSet(LogLevel loggingLevel) {
		this.newLogSet();
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		tc.setOnetimeLogLevel(loggingLevel);
	}

	/**
	 * Initialize a new log set. Use at the beginning of new transactions to
	 * start a new grouping of application logs. Any previously buffered logs
	 * will be written before starting the new log set.
	 */
	public void newLogSet() {
		Long threadId = Thread.currentThread().getId();
		TransactionInstanceCache tc = this.findTransactionInstance(threadId);
		if(tc != null) {
			this.initTransactionInstanceCache(tc);
		}
		else {
			tc = this.newTransactionInstanceCache();
			VlfLogger.instanceCache.put(threadId, tc);
			if(this.batchMode) {
				VlfLogger.batchInstance = tc;
			}
		}
	}
	
	/**
	 * Initialize a new log set with a service ID. Use at the beginning of new transactions to
	 * start a new grouping of application logs. Any previously buffered logs
	 * will be written before starting the new log set.
	 */
	public void newLogSet(String serviceId) {
		this.newLogSet();
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		tc.getCurentLogSet().setServiceId(serviceId);
	}
	
	/**
	 * Initialize a new log set. Use at the beginning of new transactions to
	 * start a new grouping of application logs. Any previously buffered logs
	 * will be written before starting the new log set.
	 * 
	 * @param serviceId
	 *            The serviceId.
	 * @param loggingLevel
	 *            The logging level.
	 */
	public void newLogSet(String serviceId, LogLevel loggingLevel) {
		this.newLogSet();
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		tc.getCurentLogSet().setServiceId(serviceId);
		tc.setOnetimeLogLevel(loggingLevel);
		tc.getCurentLogSet().setServiceId(serviceId);
	}
	
	/**
	 * Initialize a new log set. Use at the beginning of new transactions to
	 * start a new grouping of application logs. Any previously buffered logs
	 * will be written before starting the new log set.
	 * 
	 * @param serviceId
	 * The serviceId
	 * @param userId
	 * The userId
	 * @param clientHost
	 * The client host name
	 * @param loggingLevel
	 * The logging level.
	 */
	public void newLogSet(String serviceId, String userId, String clientHost, LogLevel loggingLevel) {
		this.newLogSet();
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		tc.getCurentLogSet().setServiceId(serviceId);
		tc.setOnetimeLogLevel(loggingLevel);
		tc.getCurentLogSet().setUserId(userId);
		tc.getCurentLogSet().setClientHost(clientHost);
	}
	
	/**
	 * Initialize a new log set. Use at the beginning of new transactions to
	 * start a new grouping of application logs. Any previously buffered logs
	 * will be written before starting the new log set.
	 * 
	 * @param serviceName
	 * The service name
	 * @param region
	 * The region
	 * @param zone
	 * The zone
	 * @param serviceId
	 * The serviceId
	 * @param userId
	 * The userId
	 * @param clientHost
	 * The client host name
	 * @param loggingLevel
	 * The logging level.
	 */
	public void newLogSet(String serviceName, String region, String zone,
			String serviceId, String userId, String clientHost, LogLevel loggingLevel) {
		this.newLogSet();
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		tc.getCurentLogSet().setServiceName(serviceName);
		tc.getCurentLogSet().setRegion(region);
		tc.getCurentLogSet().setZone(zone);
		tc.getCurentLogSet().setServiceId(serviceId);
		tc.setOnetimeLogLevel(loggingLevel);
		tc.getCurentLogSet().setUserId(userId);
		tc.getCurentLogSet().setClientHost(clientHost);
	}

	/**
	 * End a log set. This method generates the Overall node and writes the
	 * summary log to the database.
	 * 
	 * @param status
	 *            A Logger.Status value indicating the overall status of the log
	 *            set
	 * @param category
	 *            A Logger.Category value indicating the overall category of the
	 *            log set
	 * @param errorCode
	 *            An application specific error code
	 * @param statusMessage
	 *            A text message to provide additional details on the log set
	 *            status
	 */
	public void endLogSet(Status status, Category category, String errorCode, String statusMessage) {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		tc.getCurentLogSet().getOperationalData().setStatus(status.toString());
		tc.getCurentLogSet().getOperationalData().setCategory(category.toString());
		tc.getCurentLogSet().getOperationalData().setErrorCode(errorCode);
		tc.getCurentLogSet().getOperationalData().setStatusMsg(statusMessage);
		this.endLogSet();
	}

	/**
	 * End a log set. This method generates the Overall node and writes the
	 * summary log to the database.
	 */
	public void endLogSet() {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if (tc.getOnetimeLogLevel() == LogLevel.NONE) {
			return;
		}
		ZonedDateTime now = ZonedDateTime.now();
		tc.getCurentLogSet().setTimestamp(now);
		tc.getCurentLogSet().getOperationalData().setOperationCompleteTime(now);
		tc.getCurentLogSet().getOperationalData().setResponseTime(
				ChronoUnit.MILLIS.between(tc.getCurentLogSet().getOperationalData().getOperationTime(), 
						tc.getCurentLogSet().getOperationalData().getOperationCompleteTime()));
		try {
			logger.info(mapper.writeValueAsString(tc.getCurentLogSet()));
		} catch (JsonProcessingException e) {
			systemLogger.error(e);
		}
		if(cacheCheckTime.isAfter(LocalDateTime.now())) {
			flushInstanceCache();
		}
	}

	/**
	 * Start a new event tracking
	 * 
	 * @param appHost
	 *            The host of the target event. This can be the local host for
	 *            local events, the host name for external events, a remote URL,
	 *            etc.
	 * @param appName
	 *            The application name that owns the event.
	 * @param appService
	 *            The service name or other business function name.
	 * @param callType
	 *            The execution method of the event. Microservice, Webservice,
	 *            MQ, Database, etc.
	 * @param requestMsgSize
	 *            The data size of the requested event. Set to 0 if not
	 *            applicable.
	 * @param loggingLevel
	 *            The logging level.
	 * 
	 * @return eventId The ID of the new event. Particularly useful for nested
	 *         and overlapping events.
	 */
	public String newEvent(String appHost, String appName, String appService, String callType, int requestMsgSize,
			LogLevel loggingLevel) {
		return this.newEvent(appHost, appName, appService, callType, requestMsgSize, null, loggingLevel, Thread.currentThread().getId());
	}
	
	/**
	 * Start a new event tracking
	 * 
	 * @param appHost
	 *            The host of the target event. This can be the local host for
	 *            local events, the host name for external events, a remote URL,
	 *            etc.
	 * @param appName
	 *            The application name that owns the event.
	 * @param appService
	 *            The service name or other business function name.
	 * @param callType
	 *            The execution method of the event. Microservice, Webservice,
	 *            MQ, Database, etc.
	 * @param requestMsgSize
	 *            The data size of the requested event. Set to 0 if not
	 *            applicable.
	 * @param loggingLevel
	 *            The logging level.
	 * 
	 * @return eventId The ID of the new event. Particularly useful for nested
	 *         and overlapping events.
	 */
	public String newEvent(String appHost, String appName, String appService, String callType, int requestMsgSize,
			LogLevel loggingLevel, Long threadID) {
		return this.newEvent(appHost, appName, appService, callType, requestMsgSize, null, loggingLevel, threadID);
	}

	/**
	 * Start a new event tracking
	 * 
	 * @param appHost
	 *            The host of the target event. This can be the local host for
	 *            local events, the host name for external events, a remote URL,
	 *            etc.
	 * @param appName
	 *            The application name that owns the event.
	 * @param appService
	 *            The service name or other business function name.
	 * @param callType
	 *            The execution method of the event. Microservice, Webservice,
	 *            MQ, Database, etc.
	 * @param requestMsgSize
	 *            The data size of the requested event. Set to 0 if not
	 *            applicable.
	 * @param logId
	 *            The log ID of an associated log record. This is the value
	 *            returned from the Write method.
	 * @param loggingLevel
	 *            The logging level.
	 * 
	 * @return eventId The ID of the new event. Particularly useful for nested
	 *         and overlapping events.
	 */
	public String newEvent(String appHost, String appName, String appService, String callType, int requestMsgSize,
			String logId, LogLevel loggingLevel) {
		
		return this.newEvent(appHost, appName, appService, callType, requestMsgSize, logId, loggingLevel, Thread.currentThread().getId());
	}
	
	/**
	 * Start a new event tracking
	 * 
	 * @param appHost
	 *            The host of the target event. This can be the local host for
	 *            local events, the host name for external events, a remote URL,
	 *            etc.
	 * @param appName
	 *            The application name that owns the event.
	 * @param appService
	 *            The service name or other business function name.
	 * @param callType
	 *            The execution method of the event. Microservice, Webservice,
	 *            MQ, Database, etc.
	 * @param requestMsgSize
	 *            The data size of the requested event. Set to 0 if not
	 *            applicable.
	 * @param logId
	 *            The log ID of an associated log record. This is the value
	 *            returned from the Write method.
	 * @param loggingLevel
	 *            The logging level.
	 * 
	 * @return eventId The ID of the new event. Particularly useful for nested
	 *         and overlapping events.
	 */
	public String newEvent(String appHost, String appName, String appService, String callType, int requestMsgSize,
			String logId, LogLevel loggingLevel, Long threadId) {
		String id = UUID.randomUUID().toString();
		TransactionInstanceCache tc = this.findTransactionInstance(threadId);
		if(tc == null) {  /* failsafe -- should never happen */
			return id;
		}
		if (tc.getOnetimeLogLevel().compareTo(loggingLevel) < 0 || tc.getOnetimeLogLevel() == LogLevel.NONE) {
			return id;
		}
		OperationalData od = new OperationalData();
		od.setAppHost(appHost);
		od.setAppName(appName);
		od.setAppService(appService);
		od.setCallType(callType);
		od.setRequestMsgSize(requestMsgSize);
		if (logId != null) {
			od.setLogId(logId);
		}
		tc.getPendingEvents().put(id, od);
		return id;
	}
	
	/**
	 * Ends tracking of the specified event. Stops the timer and records the
	 * status.
	 * 
	 * @param eventId
	 *            The ID generated by the NewEvent method.
	 * @param errorCode
	 *            An application specific error code
	 * @param replyMsgSize
	 *            The data size of the event reply. Set to 0 if not applicable.
	 * @param status
	 *            A Logger.Status value indicating the status of the event
	 * @param category
	 *            A Logger.Category value indicating the category of the event
	 * @param statusMessage
	 *            A text message to provide additional details on the log set
	 *            status
	 */
	public void endEvent(String enevtId, String errorCode, int replyMsgSize, VlfLogger.Status status,
			VlfLogger.Category category, String statusMsg) {
		this.endEvent(enevtId, errorCode, replyMsgSize, status, category, statusMsg, Thread.currentThread().getId());
	}

	/**
	 * Ends tracking of the specified event. Stops the timer and records the
	 * status.
	 * 
	 * @param eventId
	 *            The ID generated by the NewEvent method.
	 * @param errorCode
	 *            An application specific error code
	 * @param replyMsgSize
	 *            The data size of the event reply. Set to 0 if not applicable.
	 * @param status
	 *            A Logger.Status value indicating the status of the event
	 * @param category
	 *            A Logger.Category value indicating the category of the event
	 * @param statusMessage
	 *            A text message to provide additional details on the log set
	 *            status
	 */
	public void endEvent(String enevtId, String errorCode, int replyMsgSize, VlfLogger.Status status,
			VlfLogger.Category category, String statusMsg, Long threadId) {
		TransactionInstanceCache tc = this.findTransactionInstance(threadId);
		if(tc == null) {  /* failsafe -- should never happen */
			return;
		}
		OperationalData od = tc.getPendingEvents().get(enevtId);
		if (od == null) {
			return;
		}
		String logData;
		od.setErrorCode(errorCode);
		od.setReplyMsgSize(replyMsgSize);
		od.setStatus(status.toString());
		od.setStatusMsg(statusMsg);
		od.setCategory(category.toString());
		od.setOperationCompleteTime(ZonedDateTime.now());
		od.setResponseTime(ChronoUnit.MILLIS.between(od.getOperationTime(), od.getOperationCompleteTime()));
		ApplicationLogSummary log = ApplicationLogSummary.cloneHeader(tc.getCurentLogSet());
		log.setOperationalData(od);
		log.setTimestamp(od.getOperationCompleteTime());
		log.setLayout(Layout.SUMMARY);
		log.setLogType(EVENT);
		try {
			logData = mapper.writeValueAsString(log);
			logger.info(logData);
		} catch (JsonProcessingException e) {
			systemLogger.error(e);
		}
	}

	/**
	 * Adds the dataKey1 entry to the log set. Should only be called by the main thread.
	 * 
	 * @param value
	 *            The value of the DataKey 
	 */
	public void addDataKey1(String value) {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if(tc == null) {  /* failsafe -- should never happen */
			return;
		}
		tc.getCurentLogSet().setDataKey1(value);
	}
	
	/**
	 * Adds the dataKey2 entry to the log set.  Should only be called by the main thread.
	 * 
	 * @param value
	 *            The value of the DataKey 
	 */
	public void addDataKey2(String value) {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if(tc == null) {  /* failsafe -- should never happen */
			return;
		}
		tc.getCurentLogSet().setDataKey2(value);
	}
	
	/**
	 * Adds the dataKey3 entry to the log set.  Should only be called by the main thread.
	 * 
	 * @param value
	 *            The value of the DataKey 
	 */
	public void addDataKey3(String value) {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if(tc == null) {  /* failsafe -- should never happen */
			return;
		}
		tc.getCurentLogSet().setDataKey3(value);
	}

	/**
	 * Adds the trackingId entry to the log set.  Should only be called by the main thread.
	 * 
	 * @param value
	 *            The value of the trackingId 
	 */
	public void addTrackingId(String trackingId) {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if(tc == null) {  /* failsafe -- should never happen */
			return;
		}
		tc.getCurentLogSet().setTrackingId(trackingId);
	}
	
	/**
	 * Adds the clientTrackingId entry to the log set.  Should only be called by the main thread.
	 * 
	 * @param value
	 *            The value of the callerTrackingId 
	 */
	public void addClientTrackingId(String callerTrackingId) {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if(tc == null) {  /* failsafe -- should never happen */
			return;
		}
		tc.getCurentLogSet().setCallerTrackingId(callerTrackingId);
	}
	
	/**
	 * Utility method to convert a String into a Map for logData
	 */
	private Map<String, String> stringToMap(String logString) {
		Map<String, String> logData = new HashMap<>();
		logData.put(TEXT, logString);
		return logData;
	}
	
	/**
	 * Writes a string to the log set.
	 * 
	 * @param operation
	 *            The logical operation name that the log record is associated
	 *            with.
	 * @param logType
	 *            The log type
	 * @param sev
	 *            A Logger.Severity entry to indicate the severity of the log
	 *            message.
	 * @param msg
	 *            A string to be logged in the DB. This string is logged as is.
	 * @param loggingLevel
	 *            The logging level.
	 * 
	 * @return Returns the log instance ID of the specific log entry.
	 */
	public String write(String operation, String logType, Severity sev, String msg, LogLevel loggingLevel) {
		return this.write(operation, logType, sev, msg, loggingLevel, Thread.currentThread().getId());
	}
	
	/**
	 * Writes a string to the log set.
	 * 
	 * @param operation
	 *            The logical operation name that the log record is associated
	 *            with.
	 * @param logType
	 *            The log type
	 * @param sev
	 *            A Logger.Severity entry to indicate the severity of the log
	 *            message.
	 * @param msg
	 *            A string to be logged in the DB. This string is logged as is.
	 * @param loggingLevel
	 *            The logging level.
	 * 
	 * @return Returns the log instance ID of the specific log entry.
	 */
	public String write(String operation, String logType, Severity sev, String msg, LogLevel loggingLevel, Long threadId) {
		TransactionInstanceCache tc = this.findTransactionInstance(threadId);
		if(tc == null && (this.logLevel.compareTo(loggingLevel) < 0 || this.logLevel == LogLevel.NONE)) { 
			return UUID.randomUUID().toString();
		}
		if (tc != null && (tc.getOnetimeLogLevel().compareTo(loggingLevel) < 0 || tc.getOnetimeLogLevel() == LogLevel.NONE)) {
			return UUID.randomUUID().toString();
		}
		String logData;
		ApplicationLogDetail detail;
		if(tc == null) { 
			detail = ApplicationLogDetail.cloneHeader(this.newApplicationLogSummary());
		}
		else {
			detail = ApplicationLogDetail.cloneHeader(tc.getCurentLogSet());
		}
		detail.setLogType(logType);
		detail.setOperation(operation);
		detail.setLogData(this.stringToMap(msg));
		detail.setLayout(Layout.DETAIL);
		try {
			logData = mapper.writeValueAsString(detail);
			if(tc != null) {
				if(logType.equals(REQUEST)) {
					tc.getCurentLogSet().getOperationalData().setRequestMsgSize(logData.length());
				}
				else if(logType.equals(RESPONSE)) {
					tc.getCurentLogSet().getOperationalData().setReplyMsgSize(logData.length());
				}
			}
			logger.info(logData);
		} catch (JsonProcessingException e) {
			systemLogger.error(e);
		}
		return detail.getLogInstanceId();
	}
	
	/**
	 * Writes a string to the log set after formatting it like the specified
	 * class.
	 * 
	 * @param operation
	 *            The logical operation name that the log record is associated
	 *            with.
	 * @param logType
	 *            The log type
	 * @param sev
	 *            A Logger.Severity entry to indicate the severity of the log
	 *            message.
	 * @param msg
	 *            Any valid JSON string to be logged in the DB. The JSON must
	 *            conform to the object structure in classType.
	 * @param loggingLevel
	 *            The logging level.
	 * 
	 * @return Returns the log ID of the specific log entry.
	 */
	public String writeJson(String operation, String logType, Severity sev, String msg, LogLevel loggingLevel) {
		return this.writeJson(operation, logType, sev, msg, loggingLevel, Thread.currentThread().getId());
	}

	/**
	 * Writes a string to the log set after formatting it like the specified
	 * class.
	 * 
	 * @param operation
	 *            The logical operation name that the log record is associated
	 *            with.
	 * @param logType
	 *            The log type
	 * @param sev
	 *            A Logger.Severity entry to indicate the severity of the log
	 *            message.
	 * @param msg
	 *            Any valid JSON string to be logged in the DB. The JSON must
	 *            conform to the object structure in classType.
	 * @param loggingLevel
	 *            The logging level.
	 * 
	 * @return Returns the log ID of the specific log entry.
	 */
	public String writeJson(String operation, String logType, Severity sev, String msg, LogLevel loggingLevel, Long threadId) {
		String logId = NA;
		TransactionInstanceCache tc = this.findTransactionInstance(threadId);
		if(tc == null && (this.logLevel.compareTo(loggingLevel) < 0 || this.logLevel == LogLevel.NONE)) { 
			return UUID.randomUUID().toString();
		}
		if (tc != null && (tc.getOnetimeLogLevel().compareTo(loggingLevel) < 0 || tc.getOnetimeLogLevel() == LogLevel.NONE)) {
			return UUID.randomUUID().toString();
		}

		try {
			Object msgObj = mapper.readValue(msg, mapStrObj);
			logId = this.write(operation, logType, sev, msgObj, loggingLevel, threadId);
		} catch (IOException e) {
			systemLogger.error(e);
		}
		return logId;
	}
	
	/**
	 * Writes an object to the log set.
	 * 
	 * @param operation
	 *            The logical operation name that the log record is associated
	 *            with.
	 * @param logType
	 *            The log type
	 * @param sev
	 *            A Logger.Severity entry to indicate the severity of the log
	 *            message.
	 * @param msg
	 *            Any valid JSON string to be logged in the DB.
	 * @param loggingLevel
	 *            The logging level.
	 * 
	 * @return Returns the log ID of the specific log entry.
	 */
	public String write(String operation, String logType, Severity sev, Object msg, LogLevel loggingLevel) {
		return this.write(operation, logType, sev, msg, loggingLevel, Thread.currentThread().getId());
	}

	/**
	 * Writes an object to the log set.
	 * 
	 * @param operation
	 *            The logical operation name that the log record is associated
	 *            with.
	 * @param logType
	 *            The log type
	 * @param sev
	 *            A Logger.Severity entry to indicate the severity of the log
	 *            message.
	 * @param msg
	 *            Any valid JSON string to be logged in the DB.
	 * @param loggingLevel
	 *            The logging level.
	 * 
	 * @return Returns the log ID of the specific log entry.
	 */
	public String write(String operation, String logType, Severity sev, Object msg, LogLevel loggingLevel, Long threadId) {
		TransactionInstanceCache tc = this.findTransactionInstance(threadId);
		if(tc == null && (this.logLevel.compareTo(loggingLevel) < 0 || this.logLevel == LogLevel.NONE)) { 
			return UUID.randomUUID().toString();
		}
		if (tc != null && (tc.getOnetimeLogLevel().compareTo(loggingLevel) < 0 || tc.getOnetimeLogLevel() == LogLevel.NONE)) {
			return UUID.randomUUID().toString();
		}
		String logData;
		ApplicationLogDetail detail;
		if(tc == null) { 
			detail = ApplicationLogDetail.cloneHeader(this.newApplicationLogSummary());
		}
		else {
			detail = ApplicationLogDetail.cloneHeader(tc.getCurentLogSet());
		}
		detail.setLogType(logType);
		detail.setOperation(operation);
		if(msg.getClass().equals(java.lang.String.class)) {
			detail.setLogData(this.stringToMap((String) msg));
		}
		else {
			detail.setLogData(msg);
		}
		detail.setLayout(Layout.DETAIL);
		try {
			logData = mapper.writeValueAsString(detail);
			if(tc != null) {
				if(logType.equals(REQUEST)) {
					tc.getCurentLogSet().getOperationalData().setRequestMsgSize(logData.length());
				}
				else if(logType.equals(RESPONSE)) {
					tc.getCurentLogSet().getOperationalData().setReplyMsgSize(logData.length());
				}
			}
			logger.info(logData);
		} catch (JsonProcessingException e) {
			systemLogger.error(e);
		}
		return detail.getLogInstanceId();
	}

	/**
	 * Writes an exception record to the log set.
	 * 
	 * @param msg
	 *            A message to be logged with the exception
	 * @param sev
	 *            A Logger.Severity entry to indicate the severity of the log
	 *            message.
	 * @param ex
	 *            The Exception object
	 * 
	 * @return Returns the log ID of the specific log entry.
	 */
	public void write(String msg, Severity sev, Exception ex) {
		this.write(msg, sev, ex, Thread.currentThread().getId());
	}
	
	/**
	 * Writes an exception record to the log set.
	 * 
	 * @param msg
	 *            A message to be logged with the exception
	 * @param sev
	 *            A Logger.Severity entry to indicate the severity of the log
	 *            message.
	 * @param ex
	 *            The Exception object
	 * 
	 * @return Returns the log ID of the specific log entry.
	 */
	public void write(String msg, Severity sev, Exception ex, Long threadId) {
		ApplicationLogDetail detail;
		TransactionInstanceCache tc = this.findTransactionInstance(threadId);
		if(tc == null) { 
			detail = ApplicationLogDetail.cloneHeader(this.newApplicationLogSummary());
		}
		else {
			detail = ApplicationLogDetail.cloneHeader(tc.getCurentLogSet());
		}
		detail.setLogType(EXCEPTION);
		detail.setOperation(detail.getServiceName());
		detail.setLayout(Layout.DETAIL);
		StringBuilder logData = new StringBuilder(LOG_BUFFER_INIT_SIZE);
		logData.append(msg + System.getProperty(LINE_SEPARATOR));
		logData.append(ex.getMessage() + System.getProperty(LINE_SEPARATOR));
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0, n = elements.length; i < n; i++) {
			logData.append(elements[i].getFileName() + ":" + elements[i].getLineNumber() + ">> "
					+ elements[i].getMethodName() + "()" + System.getProperty(LINE_SEPARATOR));
		}
		ExceptionData ed = new ExceptionData(ex.getMessage(), ex.getClass().getName(),
				ex.getCause() != null? ex.getCause().getMessage() : NA, logData.toString(), msg);
		detail.setLogData(ed);
		try {
			logger.info(mapper.writeValueAsString(detail));
		} catch (JsonProcessingException e) {
			systemLogger.error(e);
		}
	}
	
	private void initTransactionInstanceCache(TransactionInstanceCache tc) {
		tc.setOnetimeLogLevel(this.logLevel);
		tc.setIndexName(this.indexName);
		tc.setCurentLogSet(this.newApplicationLogSummary());
		tc.setLastAccessed(LocalDateTime.now());
		tc.getPendingEvents().clear();
	}
	
	private ApplicationLogSummary newApplicationLogSummary() {
		ApplicationLogSummary appSum = new ApplicationLogSummary();
		appSum.setLogId(UUID.randomUUID().toString());
		appSum.setAppName(this.appName);
		appSum.setServiceName(this.serviceName);
		appSum.setServiceId(this.serviceName);
		appSum.setHostName(VlfLogger.hostName);
		appSum.setTimestamp(ZonedDateTime.now());
		appSum.setZone(this.zone);
		appSum.setRegion(this.region);
		appSum.getOperationalData().setOperationTime(appSum.getTimestamp());
		appSum.getOperationalData().setAppService("OVERALL");
		appSum.setType(this.indexName);
		appSum.setTrackingId(appSum.getLogId());
		return appSum;
	}
	
	private TransactionInstanceCache newTransactionInstanceCache() {
		TransactionInstanceCache tc = new TransactionInstanceCache();
		tc.setOnetimeLogLevel(this.logLevel);
		tc.setIndexName(this.indexName);
		tc.setCurentLogSet(this.newApplicationLogSummary());
		tc.setLastAccessed(LocalDateTime.now());
		tc.setThreadId(Thread.currentThread().getId());
		return tc;
	}
	
	/**
	 * Retrieves the current logger state for the given thread. It will optionally 
	 * create a new instance if it is not found.
	 * 
	 * @param threadId
	 * @param createIfNotFound
	 * @return
	 */
	private TransactionInstanceCache findTransactionInstance(Long threadId) {
		TransactionInstanceCache tc;
		if(this.batchMode) {
			tc = VlfLogger.batchInstance;
		}
		else {
			tc = VlfLogger.instanceCache.get(threadId);			
		}
		return tc;
	}
	
	private static void flushInstanceCache() {
		List<Long> delQueue = new ArrayList<>();
		synchronized(VlfLogger.instanceCache) {
			for(Entry<Long, TransactionInstanceCache> currentItem : VlfLogger.instanceCache.entrySet()) {
				if(currentItem.getValue().getLastAccessed().isAfter(cacheCheckTime)) {
					delQueue.add(currentItem.getKey());
				}
			}
			for(Long deletIt : delQueue) {
				VlfLogger.instanceCache.remove(deletIt);
			}
		}
	}

	public synchronized String getAppName() {
		return appName;
	}

	public synchronized void setAppName(String appName) {
		this.appName = appName;
	}

	public static synchronized String getHostName() {
		return hostName;
	}

	public static synchronized void setHostName(String hostName) {
			VlfLogger.hostName = hostName;
	}

	public synchronized String getZone() {
		return zone;
	}

	public synchronized void setZone(String zone) {
		this.zone = zone;
	}

	public synchronized String getRegion() {
		return region;
	}

	public synchronized void setRegion(String region) {
		this.region = region;
	}

	public synchronized String getServiceName() {
		return serviceName;
	}

	public synchronized void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public synchronized String getIndexName() {
		return indexName;
	}

	public synchronized void setIndexName(String indexName) {
			this.indexName = indexName;
	}

	/**
	 * @return the logLevel
	 */
	public synchronized LogLevel getLogLevel() {
		return logLevel;
	}

	/**
	 * @param logLevel
	 *            the logLevel to set
	 */
	public synchronized void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	/** 
	 * Should only be called by the main thread.
	 * 
	 * @return the onetimeLogLevel
	 */
	public LogLevel getOnetimeLogLevel() {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if(tc == null) {  /* failsafe -- should never happen */
			return logLevel;
		}
		return tc.getOnetimeLogLevel();
	}

	/**
	 * Should only be call by the main thread.
	 * @param onetimeLogLevel
	 *            the onetimeLogLevel to set
	 */
	public void setOnetimeLogLevel(LogLevel onetimeLogLevel) {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if(tc == null) {  /* failsafe -- should never happen */
			return;
		}
		tc.setOnetimeLogLevel(onetimeLogLevel);
	}
	
	/** 
	 * Should only be called by the main thread.
	 * 
	 * @return the clientHost
	 */
	public String getClientHost() {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if(tc == null) {  /* failsafe -- should never happen */
			return NA;
		}
		return tc.getCurentLogSet().getClientHost();
	}

	/**
	 * Should only be call by the main thread.
	 * @param clientHost
	 *            the host name to set
	 */
	public void setClientHost(String clientHost) {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if(tc == null) {  /* failsafe -- should never happen */
			return;
		}
		tc.getCurentLogSet().setClientHost(clientHost);
	}
	
	/** 
	 * Should only be called by the main thread.
	 * 
	 * @return the userId
	 */
	public String getUserId() {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if(tc == null) {  /* failsafe -- should never happen */
			return NA;
		}
		return tc.getCurentLogSet().getUserId();
	}

	/**
	 * Should only be call by the main thread.
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if(tc == null) {  /* failsafe -- should never happen */
			return;
		}
		tc.getCurentLogSet().setUserId(userId);
	}
	
	/** 
	 * Should only be called by the main thread.
	 * 
	 * @return the serviceId
	 */
	public String getServiceId() {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if(tc == null) {  /* failsafe -- should never happen */
			return NA;
		}
		return tc.getCurentLogSet().getServiceId();
	}

	/**
	 * Should only be call by the main thread.
	 * @param serviceId
	 *            the service ID to set
	 */
	public void setServiceId(String serviceId) {
		TransactionInstanceCache tc = this.findTransactionInstance(Thread.currentThread().getId());
		if(tc == null) {  /* failsafe -- should never happen */
			return;
		}
		tc.getCurentLogSet().setServiceId(serviceId);
	}
	
	/** 
	 * Should only be called by the main thread.
	 * 
	 * @return the logId
	 */
	public String getLogId() {
		return this.getLogId(Thread.currentThread().getId());
	}
	
	/** 
	 * Should only be called by the main thread.
	 * @param threadId The thread ID of the main thread.
	 * 
	 * @return the logId
	 */
	public String getLogId(Long threadId) {
		TransactionInstanceCache tc = this.findTransactionInstance(threadId);
		if(tc == null) {  /* failsafe -- should never happen */
			return NA;
		}
		return tc.getCurentLogSet().getLogId();
	}
	
	
	/**
	 * @return the cachePurgeMinutes
	 */
	public static synchronized long getCachePurgeMinutes() {
		return cachePurgeMinutes;
	}
	/**
	 * @param cachePurgeMinutes the cachePurgeMinutes to set
	 */
	public static synchronized void setCachePurgeMinutes(long cachePurgeMinutes) {
		VlfLogger.cachePurgeMinutes = cachePurgeMinutes;
	}
	/**
	 * @return the batchMode
	 */
	public boolean isBatchMode() {
		return batchMode;
	}
	/**
	 * @param batchMode the batchMode to set
	 */
	public void setBatchMode(boolean batchMode) {
		this.batchMode = batchMode;
	}

}
