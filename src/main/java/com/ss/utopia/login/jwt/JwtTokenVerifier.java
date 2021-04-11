package com.ss.utopia.login.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.base.Strings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JwtTokenVerifier extends OncePerRequestFilter {

	@SuppressWarnings("unchecked")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestAuthHeader = request.getHeader(JwtUtils.getAuthorizationHeader());

		if(Strings.isNullOrEmpty(requestAuthHeader) || !requestAuthHeader.startsWith(JwtUtils.getTokenPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			String token = requestAuthHeader.replace(JwtUtils.getTokenPrefix(), "");
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(JwtUtils.getRawKey().getBytes()).build()
			.parseClaimsJws(token);
			
			String username = claims.getBody().getSubject();
			List<Map<String, String>> authorities =(List<Map<String, String>>)  claims.getBody().get("authorities");
			
			Set<SimpleGrantedAuthority> simpleGrantedAuthorities= authorities.stream()
			.map(m -> new SimpleGrantedAuthority(m.get("authority")))
			.collect(Collectors.toSet());
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					username,
					null,
					simpleGrantedAuthorities);
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		} catch(JwtException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid or expired token.");

		}
		filterChain.doFilter(request, response);
	}

}
