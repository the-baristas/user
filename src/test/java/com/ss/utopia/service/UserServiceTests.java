package com.ss.utopia.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.server.ResponseStatusException;

import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.entity.User;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserDAO dao;



	@Test
	void testGetUserById() throws ResponseStatusException {
		Optional<User> user = getUserOptional();

		when(dao.findById(1)).thenReturn(user);

		User userFromDB = userService.getUserById(1);

		assertThat(userFromDB.getUserId(), is(user.get().getUserId()));
	}
	
	@Test
	void testGetUserByIdThrowsResponseStatusException() {
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			userService.getUserById(23);});
	}
	
	//Test get by email
	
	@Test
	void testGetUserByEmail() throws ResponseStatusException {
		User user = makeUser();
		List<User> userList = new ArrayList<User>();
		userList.add(user);
		when(dao.findByUserEmail(user.getEmail())).thenReturn(userList);

		User userFromDB = userService.getUserByEmail(user.getEmail());

		assertThat(userFromDB.getEmail(), is(user.getEmail()));
	}
	@Test
	void testGetUserByEmailThrowsResponseStatusException() {
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			userService.getUserByEmail("doesntexist@nope.nop");});
	}
	
	///Test Get by username
	
	@Test
	void testGetUserByUsername() throws ResponseStatusException {
		User user = makeUser();
		List<User> userList = new ArrayList<User>();
		userList.add(user);

		when(dao.findByUsername(user.getUsername())).thenReturn(userList);

		User userFromDB = userService.getUserByUsername("someusername23");

		assertThat(userFromDB.getUsername(), is(user.getUsername()));
	}
	
	@Test
	void testGetUserByUsernameThrowsResponseStatusException() {
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			userService.getUserByUsername("someusername23");});
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
	void testUpdateUser() throws ResponseStatusException {
		Optional<User> user = getUserOptional();
		
		when(dao.findById(1)).thenReturn(user);
		when(dao.save(user.get())).thenReturn(user.get());
		
		User newUser = userService.addUser(user.get());
		
		user.get().setGivenName("ChangedFirstName");
		userService.updateUser(1, user.get());
		
		assertThat(newUser.getUserId(), is(user.get().getUserId()));
		assertThat(newUser.getGivenName(), is(user.get().getGivenName()));
	}
	
	@Test
	void testUpdateResponseStatusExceptionException() {
		User user = makeUser();
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			userService.updateUser(1, user);});
	}
	
	@Test
	void testDeleteResponseStatusExceptionException() {
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			userService.deleteUserById(23);});
	}
	
	private User makeUser() {
		User user = new User();
		user.setUserId(1);
		user.setGivenName("First");
		user.setFamilyName("Last");
		user.setUsername("someusername23");
		user.setEmail("username@email.org");
		user.setActive(true);
		user.setPhone("1111111111");
		user.setRole(2);
		user.setPassword("pass");
		return user;
	}
	
	private Optional<User> getUserOptional(){
		Optional<User> user = Optional.ofNullable(new User());
		user.get().setUserId(1);
		user.get().setGivenName("First");
		user.get().setFamilyName("Last");
		user.get().setUsername("someusername23");
		user.get().setEmail("username@email.org");
		user.get().setActive(true);
		user.get().setPhone("1111111111");
		user.get().setRole(2);
		user.get().setPassword("pass");
		
		return user;
	}

}
