package com.ss.utopia.login.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Jwts;

class JwtTokenVerifierTests {

	String secretKey = "secret32542#(*secret32542#(*secret32542#(*secret32542#(*secret32542#(*";
	
	//Build tokens
	String adminToken = Jwts.builder().setSubject("admin")
		.claim("authorities", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
		.setIssuedAt(new Date())
		.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(JwtUtils.getTokenExpirationAfterDays())))
		.signWith(JwtUtils.getSecretKey(secretKey))
		.compact();
	String customerToken = Jwts.builder().setSubject("customer")
			.claim("authorities", Arrays.asList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
			.setIssuedAt(new Date())
			.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(JwtUtils.getTokenExpirationAfterDays())))
			.signWith(JwtUtils.getSecretKey(secretKey))
			.compact();
	
	JwtTokenVerifier tokenVerifier = new JwtTokenVerifier(secretKey);
	
	@Test
	void testGetUsernameFromToken() {
		assertEquals("admin", tokenVerifier.getUsernameFromToken(adminToken));
		assertEquals("customer", tokenVerifier.getUsernameFromToken(customerToken));
	}
	
	@Test
	void testGetRoleFromToken() {
		assertEquals("ROLE_ADMIN", tokenVerifier.getRoleFromToken(adminToken));
		assertEquals("ROLE_CUSTOMER", tokenVerifier.getRoleFromToken(customerToken));
	}

}
