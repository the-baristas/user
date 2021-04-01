package com.ss.utopia.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid email format")
public class EmailException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailException(String message) {
		super(message);
	}
}
