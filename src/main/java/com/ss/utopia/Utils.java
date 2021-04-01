package com.ss.utopia;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


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
	
}
