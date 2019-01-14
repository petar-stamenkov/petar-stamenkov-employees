package com.sirmatask.exceptions;

public class TasksParseException extends Exception {
	
	private final String EXCEPTION_MESSAGE = "Invalid file format.";
	
	@Override
	public String getMessage() {
		
		return EXCEPTION_MESSAGE;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3480920004282295747L;

}
