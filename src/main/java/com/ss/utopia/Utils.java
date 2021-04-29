package com.ss.utopia;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;


public class Utils {
	
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public static void checkEmailValid(String email) throws ResponseStatusException {
		Pattern pattern = Pattern.compile("^[a-z+[0-9]]{1,}[@][a-z+[0-9]]{1,}[\\.][a-z+[0-9]]{1,}$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		if(!matcher.find())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Invalid email format: " + email);

	}
	
	public static void checkPhoneNumberValid(String phoneNumber) throws ResponseStatusException{
		Pattern pattern = Pattern.compile("^\\d{10}$");
		Matcher matcher = pattern.matcher(phoneNumber);
		if(!matcher.find())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Phone number should be 10 digits and contain no hyphens, spaces, etc.");
	}
	
}
