package com.dataflow.flow.centric.lib.exceptions;

/**
 * Exception arisen when any Stream exception occurs
 * @author Fabrizio Torelli
 */
public class IOFlowException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8197390655372941013L;

	/**
	 * Default Constructor
	 */
	public IOFlowException() {
		super();
	}

	/**
	 * Extensive Constructor
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public IOFlowException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Message/Cause Constructor
	 * @param message
	 * @param cause
	 */
	public IOFlowException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Message Constructor
	 * @param message
	 */
	public IOFlowException(String message) {
		super(message);
	}

	/**
	 * Cause Constructor
	 * @param cause
	 */
	public IOFlowException(Throwable cause) {
		super(cause);
	}


}
