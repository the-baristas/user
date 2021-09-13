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

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Strings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class JwtTokenVerifier extends OncePerRequestFilter {

	private String rawSecretKey;

	public JwtTokenVerifier(String rawSecretKey) {
		super();
		this.rawSecretKey = rawSecretKey;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestAuthHeader = request.getHeader(JwtUtils.getAuthorizationHeader());

		if (Strings.isNullOrEmpty(requestAuthHeader) || !requestAuthHeader.startsWith(JwtUtils.getTokenPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}

		// Claims represents the fields of a JWT (sub, authorities, etc) as an object
		Jws<Claims> claims = getJwsClaims(requestAuthHeader);

		// Retrieve the necessary fields
		String username = claims.getBody().getSubject();
		List<Map<String, String>> authorities = (List<Map<String, String>>) claims.getBody().get("authorities");

		// Build the simpleGrantedAuthorities
		Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
				.map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toSet());

		// Set the security context for the current session to reflect this token
		Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
				simpleGrantedAuthorities);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

	private Jws<Claims> getJwsClaims(String bearerToken) {
		// Retrieve the token from the header, and remove the "Bearer: " portion from
		// the token
		String token = bearerToken.replace(JwtUtils.getTokenPrefix(), "");

		// Claims represents the fields of a JWT (sub, authorities, etc) as an object
		return Jwts.parserBuilder().setSigningKey(rawSecretKey.getBytes()).build().parseClaimsJws(token);
	}

	public String getUsernameFromToken(String bearerToken) {
		Jws<Claims> claims = getJwsClaims(bearerToken);

		// Retrieve subject (username)
		return claims.getBody().getSubject();
	}

	@SuppressWarnings("unchecked")
	public String getRoleFromToken(String bearerToken) {
		Jws<Claims> claims = getJwsClaims(bearerToken);
		List<Map<String, String>> authorities = (List<Map<String, String>>) claims.getBody().get("authorities");

		return authorities.get(0).get("authority");
	}

}
