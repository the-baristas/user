package com.ss.utopia.login;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.entity.User;
import com.ss.utopia.entity.UserRole;

@ExtendWith(MockitoExtension.class)
public class UtopiaUserDetailsServiceTests {
	
	@InjectMocks
	private UtopiaUserDetailsService userDetailsService;

	@Mock
	private UserDAO dao;
	
	@Test
	void testLoadUserByUsername() {
		User user = new User();
		user.setUserId(1);
		user.setGivenName("First");
		user.setFamilyName("Last");
		user.setUsername("someusername23");
		user.setEmail("username@email.org");
		user.setActive(true);
		user.setPhone("1111111111");
		user.setRole(new UserRole(2, "ROLE_ADMIN"));
		user.setPassword("pass");
		
		when(dao.findByUsername(user.getUsername())).thenReturn(Arrays.asList(user));	
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
		assertThat(userDetails.getUsername(), is(user.getUsername()));
	}
	
	@Test
	void testLoadUserByUsernameThrowsUsernameNotFoundException() {
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			userDetailsService.loadUserByUsername("nopenotreal");});
	}
	
}
