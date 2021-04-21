package com.ss.utopia.login.jwt;

public class UserAuthenticationRequest {

	private String username;
	private String password;
	
	public UserAuthenticationRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public UserAuthenticationRequest() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
