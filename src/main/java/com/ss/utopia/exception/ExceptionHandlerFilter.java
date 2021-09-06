package com.ss.utopia.exception;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

//This filter is necessary since @ControllerAdvice can't catch exceptions in the security filters
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	
	@Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(e.getMessage());
    }
}

}
