package com.ss.utopia.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.entity.User;
import com.ss.utopia.exception.EmailException;
import com.ss.utopia.exception.UserNotFoundException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserService.class)
@AutoConfigureMockMvc
class UserServiceTests {

	@InjectMocks
	private UserService userService;

	@MockBean
	private UserDAO dao;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetUserById() throws UserNotFoundException {
		Optional<User> user = Optional.ofNullable(new User());
		user.get().setUserId(1);
		user.get().setGivenName("First");
		user.get().setFamilyName("Last");
		user.get().setUsername("someusername12");
		user.get().setEmail("aa@email.com");
		user.get().setIsActive(true);
		user.get().setPhone("1111111111");
		user.get().setRole(2);
		user.get().setPassword("pass");

		when(dao.findById(1)).thenReturn(user);

		User userFromDB = userService.getUserById(1);

		assertThat(userFromDB.getUserId(), is(user.get().getUserId()));
	}
	
	@Test
	void testGetUserByIdThrowsUserNotFoundException() {
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			userService.getUserById(23);});
	}

	@Test
	void testAddUserWithInvalidEmailException() {
		User user = makeUser();

		user.setEmail("invalidemail.com");

		assertThrows(ResponseStatusException.class, () -> {
			userService.addUser(user);
		});

		user.setEmail("email@invalid..net");
		assertThrows(ResponseStatusException.class, () -> {
			userService.addUser(user);
		});
	}
	
	@Test
	void testUpdateUser() throws EmailException, UserNotFoundException {
		Optional<User> user = Optional.ofNullable(new User());
		user.get().setUserId(1);
		user.get().setGivenName("First");
		user.get().setFamilyName("Last");
		user.get().setUsername("someusername12");
		user.get().setEmail("aa@email.com");
		user.get().setIsActive(true);
		user.get().setPhone("1111111111");
		user.get().setRole(2);
		user.get().setPassword("pass");
		
		when(dao.findById(1)).thenReturn(user);
		when(dao.save(user.get())).thenReturn(user.get());
		
		User newUser = userService.addUser(user.get());
		
		user.get().setGivenName("ChangedFirstName");
		userService.updateUser(user.get());
		
		assertThat(newUser.getUserId(), is(user.get().getUserId()));
		assertThat(newUser.getGivenName(), is(user.get().getGivenName()));
	}
	
	@Test
	void testUpdateUserNoSuchElementException() {
		User user = makeUser();
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			userService.updateUser(user);});
	}
	
	@Test
	void testDeleteUserNoSuchElementException() {
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			userService.deleteUserById(23);});
	}
	
	private User makeUser() {
		User user = new User();
		user.setUserId(1);
		user.setGivenName("First");
		user.setFamilyName("Last");
		user.setUsername("someUsername23");
		user.setEmail("username@email.org");
		user.setIsActive(true);
		user.setPhone("1111111111");
		user.setRole(2);
		user.setPassword("pass");
		return user;
	}

}
