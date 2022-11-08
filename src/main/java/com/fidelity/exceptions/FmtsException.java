package com.fidelity.exceptions;

public class FmtsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FmtsException() {
		super();

	}

	public FmtsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	public FmtsException(String message, Throwable cause) {
		super(message, cause);

	}

	public FmtsException(String message) {
		super(message);

	}

	public FmtsException(Throwable cause) {
		super(cause);

	}

}
