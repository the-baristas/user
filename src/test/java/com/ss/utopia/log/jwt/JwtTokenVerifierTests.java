package com.ss.utopia.log.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ss.utopia.login.jwt.JwtTokenVerifier;
import com.ss.utopia.login.jwt.JwtUtils;

import io.jsonwebtoken.Jwts;

class JwtTokenVerifierTests {

	//Build tokens
	String adminToken = Jwts.builder().setSubject("admin")
		.claim("authorities", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
		.setIssuedAt(new Date())
		.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(JwtUtils.getTokenExpirationAfterDays())))
		.signWith(JwtUtils.getSecretKey())
		.compact();
	String customerToken = Jwts.builder().setSubject("customer")
			.claim("authorities", Arrays.asList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
			.setIssuedAt(new Date())
			.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(JwtUtils.getTokenExpirationAfterDays())))
			.signWith(JwtUtils.getSecretKey())
			.compact();
	
	JwtTokenVerifier tokenVerifier = new JwtTokenVerifier();
	
	@Test
	void testGetUsernameFromToken() {
		assertEquals(tokenVerifier.getUsernameFromToken(adminToken), "admin");
		assertEquals(tokenVerifier.getUsernameFromToken(customerToken), "customer");
	}
	
	@Test
	void testGetRoleFromToken() {
		assertEquals(tokenVerifier.getRoleFromToken(adminToken), "ROLE_ADMIN");
		assertEquals(tokenVerifier.getRoleFromToken(customerToken), "ROLE_CUSTOMER");
	}

}
