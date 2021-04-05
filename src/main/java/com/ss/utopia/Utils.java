package com.ss.utopia;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ss.utopia.exception.EmailException;


public class Utils {
	
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public static void checkEmailValid(String email) throws EmailException {
		Pattern pattern = Pattern.compile("^[a-z+[0-9]]{1,}[@][a-z+[0-9]]{1,}[\\.][a-z+[0-9]]{1,}$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		if(!matcher.find())
			throw new EmailException("Not a valid email address format");

	}
	
}
