package com.ss.utopia.exception;

public class ConfirmationExpiredException extends Exception {

	private static final long serialVersionUID = 1L;
	private String userEmail;
	
    public ConfirmationExpiredException() {
    }

    public ConfirmationExpiredException(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public String getUserEmail() {
    	return this.userEmail;
    }
}
