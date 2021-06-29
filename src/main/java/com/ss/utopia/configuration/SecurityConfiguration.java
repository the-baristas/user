package com.ss.utopia.configuration;

import com.ss.utopia.login.UtopiaUserDetailsService;
import com.ss.utopia.login.jwt.JwtTokenVerifier;
import com.ss.utopia.login.jwt.JwtUserAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Value("${jwt.secretKey}")
    private String jwtSecretKey;
	
	@Autowired
	UtopiaUserDetailsService utopiaUserDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(utopiaUserDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.cors().and()
        .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
		.sessionManagement()
		.and()
		.addFilter(new JwtUserAuthenticationFilter(authenticationManager(), jwtSecretKey))
		.addFilterAfter(new JwtTokenVerifier(jwtSecretKey), JwtUserAuthenticationFilter.class)
		.authorizeRequests()
		.antMatchers(HttpMethod.POST, "/users").permitAll()
		.antMatchers(HttpMethod.GET, "/users/health").permitAll()
        .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
		.anyRequest().authenticated();
		
		
		
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

}
