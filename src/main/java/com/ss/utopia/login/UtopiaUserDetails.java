package com.ss.utopia.login;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ss.utopia.entity.User;

public class UtopiaUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private boolean isActive;
	private List<GrantedAuthority> authorities;

	public UtopiaUserDetails(String username) {
		this.username = username;
	}

	public UtopiaUserDetails(User user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole().getRoleName()));
		this.isActive = user.isActive();
	}

	public UtopiaUserDetails() {

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
		//return Utils.passwordEncoder().encode(password);
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return isActive;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return isActive;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return isActive;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return isActive;
	}

}
