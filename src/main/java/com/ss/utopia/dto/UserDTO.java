package com.ss.utopia.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
	
	
	private int userId;
	@NotBlank(message ="Must include a first name.")
	private String givenName;
	@NotBlank(message = "Must include a last name.")
	private String familyName;
	@NotBlank(message = "Must include a username.")
	private String username;

	@NotNull(message = "Must include a email.")
	private String email;
	@NotBlank(message = "Must include a password.")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	@NotBlank(message = "Must include a phone number.")
	private String phone;
	
	private String role;
	
	private boolean isActive;
	
	public int getUserId() {
		return userId;
	}
	public String getGivenName() {
		return givenName;
	}
	public String getFamilyName() {
		return familyName;
	}
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
	}
	public String getRole() {
		return role;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
