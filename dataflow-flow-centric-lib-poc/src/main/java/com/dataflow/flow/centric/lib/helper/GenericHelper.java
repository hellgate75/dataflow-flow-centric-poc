package com.dataflow.flow.centric.lib.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.bson.BsonDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.core.lib.logger.VlfLogger.LogLevel;
import com.dataflow.core.lib.logger.VlfLogger.Severity;

/**
 * @author Administrator (Fabrizio Torelli - hellgate75@gmail.com)
 */
public class GenericHelper {

	public static final Logger log = LoggerFactory.getLogger(GenericHelper.class);

	private GenericHelper() {
		// No Argument Constructor - Should not be invoked
	}
	
	/**
	 * Remove tailing zeros, creating a valid number format
	 * @param in
	 * @return
	 */
	public static final String removeTrailingZeros(String in) {
		String out = in;
		while (out.endsWith("0") && out.length() > 1) {
			out = out.substring(0, out.length()-2);
		}
		return out;
	}

	/**
	 * Convert a stack trace to representing string listing all stack of exceptions
	 * @param stackTraceElements
	 * @return
	 */
	public static final String convertStackTrace(StackTraceElement[] stackTraceElements) {
		String stackTraceStr = "";
		if (stackTraceElements==null || stackTraceElements.length==0)
			return stackTraceStr;
		for (StackTraceElement stackTraceElement : stackTraceElements) {
			stackTraceStr += stackTraceElement.toString() + "\n";
		}
		return stackTraceStr;
	}
	
	private static final String LOG_OPERATION = "Validate-Output";
	private static final String RESPONSE = "RESPONSE";
	
	/**
	 * Remove heading zeros, creating a valid number format
	 * @param in
	 * @return
	 */
	public static final String removeHeadingZeros(String s) {
		while (s != null && s.trim().length() > 1 && s.trim().startsWith("0") ) {
			s = s.trim().substring(1);
		}
		return s;
	}
	
	/**
	 * Sanitize Unicode characters from strings
	 * @param s Input String
	 * @return Unicode characters sanitized output string
	 */
	public static final String fixUnicodeCharacters(String s) {
//		return s.replaceAll("[^\\x00-\\x7F]", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "").replaceAll("\\p{C}", "").replaceAll("\\p{Cf}", "").replaceAll("\\", "");
		return s.replaceAll("\\p{C}", "").replaceAll("\\p{Cf}", "");
	}
	
	/**
	 * Method that securely parse a String to integer value, and in case of not null logger, logs eventually the error
	 * @param s Input String
	 * @param vlfLogger Logger for error logging, or null in case log is not necessary
	 * @return (int) Parsed integer value, or default 0 value in case of error.
	 */
	public static final int parseInt(String s, VlfLogger vlfLogger) {
		int out = 0;
		// remove heading zeros from input string because they don't allow input string to be 
		// parsed as integer value (prevent unwanted errors (input file data format issues)
		try {
			s = removeHeadingZeros(fixUnicodeCharacters(s));
		} catch (Exception e) {
			if ( vlfLogger != null ) {
				vlfLogger.write(LOG_OPERATION, RESPONSE, Severity.ERROR,
						String.format("GenericHelper::parseInt string cleaning error : %s -> $s", e.getMessage(), convertStackTrace(e.getStackTrace())), 
						LogLevel.ERROR);
				
			} else {
				System.err.println(String.format("GenericHelper::parseInt string cleaning error : %s -> $s", e.getMessage(), convertStackTrace(e.getStackTrace())));
			}
		}
		try {
			out = Integer.parseInt(s);
		} catch (Exception e) {
			if ( vlfLogger != null ) {
				vlfLogger.write(LOG_OPERATION, RESPONSE, Severity.ERROR,
						String.format("GenericHelper::parseInt error : %s -> $s", e.getMessage(), convertStackTrace(e.getStackTrace())), 
						LogLevel.ERROR);
				
			} else {
				System.err.println(String.format("GenericHelper::parseInt error : %s -> $s", e.getMessage(), convertStackTrace(e.getStackTrace())));
			}
		}
		return out;
	}

	/**
	 * Method that securely parse a String to long value, and in case of not null logger, logs eventually the error
	 * @param s Input String
	 * @param vlfLogger Logger for error logging, or null in case log is not necessary
	 * @return (long) Parsed integer value, or default 0 value in case of error.
	 */
	public static final long parseLong(String s, VlfLogger vlfLogger) {
		long out = 0l;
		// remove heading zeros from input string because they don't allow input string to be 
		// parsed as integer value (prevent unwanted errors (input file data format issues)
		try {
			s = removeHeadingZeros(fixUnicodeCharacters(s));
		} catch (Exception e) {
			if ( vlfLogger != null ) {
				vlfLogger.write(LOG_OPERATION, RESPONSE, Severity.ERROR,
						String.format("GenericHelper::parseLong string cleaning error : %s -> $s", e.getMessage(), convertStackTrace(e.getStackTrace())), 
						LogLevel.ERROR);
				
			} else {
				System.err.println(String.format("GenericHelper::parseLong string cleaning error : %s -> $s", e.getMessage(), convertStackTrace(e.getStackTrace())));
			}
		}
		try {
			out = Long.parseLong(s);
		} catch (Exception e) {
			if ( vlfLogger != null ) {
				vlfLogger.write(LOG_OPERATION, RESPONSE, Severity.ERROR,
						String.format("GenericHelper::parseLong error : %s -> $s", e.getMessage(), convertStackTrace(e.getStackTrace())), 
						LogLevel.ERROR);
				
			} else {
				System.err.println(String.format("GenericHelper::parseLong error : %s -> $s", e.getMessage(), convertStackTrace(e.getStackTrace())));
			}
		}
		return out;
	}

	/**
	 * Method that securely parse a String to double value, and in case of not null logger, logs eventually the error
	 * @param s Input String
	 * @param vlfLogger Logger for error logging, or null in case log is not necessary
	 * @return (double) Parsed integer value, or default 0 value in case of error.
	 */
	public static final double parseDouble(String s, VlfLogger vlfLogger) {
		double out = 0d;
		// remove heading zeros from input string because they don't allow input string to be 
		// parsed as integer value (prevent unwanted errors (input file data format issues)
		try {
			s = removeHeadingZeros(fixUnicodeCharacters(s));
		} catch (Exception e) {
			if ( vlfLogger != null ) {
				vlfLogger.write(LOG_OPERATION, RESPONSE, Severity.ERROR,
						String.format("GenericHelper::parseDouble string cleaning error : %s -> $s", e.getMessage(), convertStackTrace(e.getStackTrace())), 
						LogLevel.ERROR);
				
			} else {
				System.err.println(String.format("GenericHelper::parseDouble string cleaning error : %s -> $s", e.getMessage(), convertStackTrace(e.getStackTrace())));
			}
		}
		try {
			out = Double.parseDouble(s);
		} catch (Exception e) {
			if ( vlfLogger != null ) {
				vlfLogger.write(LOG_OPERATION, RESPONSE, Severity.ERROR,
						String.format("GenericHelper::parseDouble error : %s -> $s", e.getMessage(), convertStackTrace(e.getStackTrace())), 
						LogLevel.ERROR);
				
			} else {
				System.err.println(String.format("GenericHelper::parseDouble error : %s -> $s", e.getMessage(), convertStackTrace(e.getStackTrace())));
			}
		}
		return out;
	}

	
	/**
	 * @return
	 */
	public static final String getHostNameAndIpAddressString() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			return String.format("%s / %s", ip.getHostAddress(), ip.getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @return
	 */
	public static final String getHostNameString() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @return
	 */
	public static final String getIpAddressString() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @return
	 */
	public static final String getUserNameString() {
		return System.getProperty("user.name");
	}
	
	/**
	 * @return
	 */
	public synchronized static final String getDateToken() {
		return "" + new Date(System.nanoTime()).getTime();
	}

	public static final void sleepThread(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			//INFO: NOT INPORTANT
		}
	}

	public static final BsonDocument jsonTextToBSON(String text) {
		return BsonDocument.parse(text);
	}
	
}
