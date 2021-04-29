package com.ss.utopia.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
	
	@Positive
	private int userId;
	@NotBlank
	private String givenName;
	@NotBlank
	private String familyName;
	@NotBlank
	private String username;
	@Email
	@NotNull
	private String email;
	@NotBlank
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	@NotBlank
	private String phone;
	@NotNull
	private String role;
	@NotNull
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
