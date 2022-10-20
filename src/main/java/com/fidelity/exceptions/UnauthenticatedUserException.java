package com.fidelity.exceptions;

public class UnauthenticatedUserException extends Exception{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnauthenticatedUserException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnauthenticatedUserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public UnauthenticatedUserException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnauthenticatedUserException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnauthenticatedUserException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	
}
