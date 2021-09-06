package com.ss.utopia.exception;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;


@ControllerAdvice
public class ApplicationExceptionHandler {
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	ResponseEntity<Error> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
		String message = exception.getMessage();
		String constraintType = message.substring(
				message.lastIndexOf("[")+1, message.indexOf("_UNIQUE"));
		Error error = new Error(HttpStatus.CONFLICT.value(),
				"A user with this " + constraintType + " already exists.");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}
    
    @ExceptionHandler(ConfirmationExpiredException.class)
    void handleConfirmationExpiredException(ConfirmationExpiredException exception){
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
				"This confirmation code has expired. Another email will be sent to " + exception.getUserEmail());
    }
    
    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<Void> handleResponseStatusException(
            ResponseStatusException exception) {
        throw exception;
    }
    

    
    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<String> handleAuthenticationException(
    		AuthenticationException exception){
    	exception.printStackTrace();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		        .body("This account is locked. If you haven't yet, please verify your account.");
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<String> handleBadCredentialsException(
    		BadCredentialsException exception){
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Incorrect username and/or password.");
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Error methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }
    private Error processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        Error error = new Error(HttpStatus.BAD_REQUEST.value(), fieldErrors.get(0).getDefaultMessage());
        return error;
    }
    static class Error {
        private final int status;
        private final String message;
        
        Error(int status, String message) {
            this.status = status;
            this.message = message;
        }
        public int getStatus() {
            return status;
        }
        public String getMessage() {
            return message;
        }

    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUncaughtException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unknown error occurred.");
    }
}