package com.ss.utopia.login.jwt;

import javax.crypto.SecretKey;


import com.google.common.net.HttpHeaders;
import com.ss.utopia.configuration.SecurityConfiguration;

import io.jsonwebtoken.security.Keys;

public class JwtUtils {
	
	private JwtUtils() {}

	private static String secretKey = "202this320#@3mis209goodc3m0kenoughdWPD<MW@3-90i1#)%4820jffFWEQ3291-12";
	private static String tokenPrefix = "Bearer ";
	private static Integer tokenExpirationAfterDays = 14;
	
	
	public static String getRawKey() {
		return secretKey;
	}
	
	public static String getTokenPrefix() {
		return tokenPrefix;
	}
	public static Integer getTokenExpirationAfterDays() {
		return tokenExpirationAfterDays;
	}	
	
	public static String getAuthorizationHeader() {
		return HttpHeaders.AUTHORIZATION;
	}
	
	public static SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}

}
