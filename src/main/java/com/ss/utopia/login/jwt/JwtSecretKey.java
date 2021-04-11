package com.ss.utopia.login.jwt;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtSecretKey {
	
	private JwtConfiguration jwtConfiguration;

	
	@Autowired
	public JwtSecretKey(JwtConfiguration jwtConfiguration) {
		this.jwtConfiguration = jwtConfiguration;
	}
	
	@Bean
	public SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtConfiguration.getSecretKey().getBytes());
	}
}
