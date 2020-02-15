/**
 * 
 */
package com.dataflow.flow.centric.lib.helper;

import com.dataflow.core.lib.logger.VlfLogger;
import com.dataflow.flow.centric.lib.constants.GlobalConstants;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public final class LoggerHelper {

	private LoggerHelper() {
		super();
	}

	/**
	 * Log debug information to {@link VlfLogger} and report information level message
	 * @param vlfLogger logger bean
	 * @param place Descriptive information for log raise code place
	 * @param message Message to be reported
	 */
	public static void logDebug(VlfLogger vlfLogger, String place, String message) {
		if ( vlfLogger != null )
			vlfLogger.write(GlobalConstants.LOG_OPERATION, VlfLogger.Category.SUCCESS.name(), VlfLogger.Severity.INFO,
					String.format("%s::info %s'", place, message), VlfLogger.LogLevel.VERBOSE);
		else
			System.out.println("VERBOSE - " + 
					String.format("%s::info %s'", place, message)
			);
	}

	/**
	 * Log information to {@link VlfLogger} and report information level message
	 * @param vlfLogger logger bean
	 * @param place Descriptive information for log raise code place
	 * @param message Message to be reported
	 */
	public static void logInfo(VlfLogger vlfLogger, String place, String message) {
		if ( vlfLogger != null )
			vlfLogger.write(GlobalConstants.LOG_OPERATION, VlfLogger.Category.SUCCESS.name(), VlfLogger.Severity.INFO,
					String.format("%s::info %s'", place, message), VlfLogger.LogLevel.INFO);
		else
			System.out.println("INFO - " + 
					String.format("%s::info %s'", place, message)
			);
	}

	/**
	 * Log informations to {@link VlfLogger} and report warning level message
	 * @param vlfLogger logger bean
	 * @param place Descriptive information for log raise code place
	 * @param message Message to be reported
	 * @param ex Exception to report
	 */
	public static void logWarning(VlfLogger vlfLogger, String place, String message, Exception ex) {
		if ( ex != null ) {
			if ( vlfLogger != null )
				vlfLogger.write(GlobalConstants.LOG_OPERATION, VlfLogger.Category.SUCCESS.name(), VlfLogger.Severity.WARNING,
						String.format("%s::warn -> class: %s, message: %s -> %s'", place, ex.getClass().getCanonicalName(), ex.getMessage(), GenericHelper.convertStackTrace(ex.getStackTrace())), VlfLogger.LogLevel.WARN);
			else
				System.out.println(String.format("%s::warn -> class: %s,  message: %s -> %s", place, ex.getClass().getCanonicalName(), ex.getMessage(), GenericHelper.convertStackTrace(ex.getStackTrace())));
		}
		if ( message != null && ! message.trim().isEmpty()) {
			if ( vlfLogger != null )
				vlfLogger.write(GlobalConstants.LOG_OPERATION, VlfLogger.Category.SUCCESS.name(), VlfLogger.Severity.WARNING,
						String.format("%s::warn %s'", place, message), VlfLogger.LogLevel.WARN);
			else
				System.out.println(String.format("%s::warn %s", place, message));
		}
		if ( ex == null && (message == null || message.trim().isEmpty()) ) {
				if ( vlfLogger != null )
					vlfLogger.write(GlobalConstants.LOG_OPERATION, VlfLogger.Category.SUCCESS.name(), VlfLogger.Severity.WARNING,
							String.format("%s::warn WARNING!!!'", place), VlfLogger.LogLevel.WARN);
				else
					System.out.println(String.format("%s::warn WARNING!!!", place));
		} 
	}

	/**
	 * Log informations to {@link VlfLogger} and report error level message
	 * @param vlfLogger logger bean
	 * @param place Descriptive information for log raise code place
	 * @param message Message to be reported
	 * @param errorCategory {@linkplain VlfLogger.Category reporting the kind of error
	 * @param ex Exception to report
	 */
	public static void logError(VlfLogger vlfLogger, String place, String message, VlfLogger.Category errorCategory, Exception ex) {
		if ( ex != null ) {
			if ( vlfLogger != null )
				vlfLogger.write(GlobalConstants.LOG_OPERATION, errorCategory.name(), VlfLogger.Severity.ERROR,
						String.format("%s::error -> class: %s, message: %s -> %s'", place, ex.getClass().getCanonicalName(), ex.getMessage(), GenericHelper.convertStackTrace(ex.getStackTrace())), VlfLogger.LogLevel.ERROR);
			else
				System.err.println(String.format("%s::error -> class: %s,  message: %s -> %s", place, ex.getClass().getCanonicalName(), ex.getMessage(), GenericHelper.convertStackTrace(ex.getStackTrace())));
		}
		if ( message != null && ! message.trim().isEmpty()) {
			if ( vlfLogger != null )
				vlfLogger.write(GlobalConstants.LOG_OPERATION, errorCategory.name(), VlfLogger.Severity.ERROR,
						String.format("%s::error %s'", place, message), VlfLogger.LogLevel.ERROR);
			else
				System.err.println(String.format("%s::error %s", place, message));
			
		}
		if ( ex == null && (message == null || message.trim().isEmpty()) ) {
				if ( vlfLogger != null )
					vlfLogger.write(GlobalConstants.LOG_OPERATION, errorCategory.name(), VlfLogger.Severity.ERROR,
							String.format("%s::error ERROR!!!'", place), VlfLogger.LogLevel.ERROR);
				else
					System.err.println(String.format("%s::error ERROR!!!", place));
					
			}
		} 
}
