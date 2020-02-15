package com.dataflow.flow.centric.lib.exceptions;

/**
 * Exception arisen when any flow Centric generic exception occurs
 * @author Fabrizio Torelli
 */
public class FlowProcessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5427501230719148653L;

	/**
	 * Default Constructor
	 */
	public FlowProcessException() {
		super();
	}

	/**
	 * Extensive Constructor
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public FlowProcessException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Message/Cause Constructor
	 * @param message
	 * @param cause
	 */
	public FlowProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Message Constructor
	 * @param message
	 */
	public FlowProcessException(String message) {
		super(message);
	}

	/**
	 * Cause Constructor
	 * @param cause
	 */
	public FlowProcessException(Throwable cause) {
		super(cause);
	}


}
