package com.ss.utopia;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ss.utopia.exception.EmailException;


public class Utils {
	
	
	public static void main(String[] args) {
		//$2a$10$fvGqZpG79iEl.F0ROcUTFueCwidTpkxMXirAxWpr.d4XADht2Zc26
		String s = "password";
		String es = "$2a$10$39OKUwAauZYXJX2Qp84NYOXhXI3qMuWaBbYZjQUX4qqZeAsiVkWGe";
		String es2 = "$2a$10$39OKUwAauZFGJX2Qp84NYOXhXI3qMuWaBbYZjQUX4qqZeAsiVkWGe";
		System.out.println(passwordEncoder().matches(s, es));
		System.out.println(passwordEncoder().matches(s, es2));
		
		
	}
	
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
