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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.util.concurrent.Service;
import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.entity.User;
import com.ss.utopia.entity.UserRole;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserDAO dao;

	
	@Test
	void testGetAllUsersPageable(){
		List<User> users = new ArrayList<User>();
		User user1 = makeUser();
		User user2 = makeUser();
		user2.setUserId(2);
		user2.setEmail("bb@gmail.com");
		user2.setUsername("username4567");
		user2.setPhone("8195678900");
		users.add(user1);
		users.add(user2);
	
		PageRequest pageRequest = PageRequest.of(0, 2);
		Page<User> userPage = new PageImpl<User>(users);
		
		when(dao.findAll(pageRequest)).thenReturn(userPage);
		
		assertThat(userService.getAllUsers(0,2), is(userPage));
		
	}


	@Test
	void testGetUserById() throws ResponseStatusException {
		Optional<User> user = getUserOptional();

		when(dao.findById(1)).thenReturn(user);

		User userFromDB = userService.getUserById(1);

		assertTrue(userFromDB.equals(user.get()));
	}
	
	@Test
	void testGetUserByIdThrowsResponseStatusException() {
		Assertions.assertThrows(ResponseStatusException.class, () -> {userService.getUserById(23);});
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
	
	//Test Get by phone number
	
	@Test
	void testGetUserByPhoneNumber() throws ResponseStatusException {
		User user = makeUser();
		List<User> userList = new ArrayList<User>();
		userList.add(user);
;
		when(dao.findByPhoneNumber(user.getPhone())).thenReturn(userList);

		User userFromDB = userService.getUserByPhoneNumber("1111111111");

		assertThat(userFromDB.getPhone(), is(user.getPhone()));
	}
	
	@Test
	void testGetUserByPhoneNumberThrowsResponseStatusException() {
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			userService.getUserByPhoneNumber("1111111111");});
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
	
	@Test
	void testCheckNoDuplicateFields() {
		List<User> users = new ArrayList<User>();
		User user1 = makeUser();
		User user2 = makeUser();
		user2.setUserId(2);
		user2.setEmail("bb@gmail.com");
		user2.setUsername("username4567");
		user2.setPhone("8195678900");
		users.add(user1);
		users.add(user2);
		
		User newUser = makeUser();
		newUser.setUserId(3);
		
		when(dao.findAll()).thenReturn(users);
		
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			userService.addUser(newUser);});
		
		newUser.setEmail("unique@unique.uni");
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			userService.addUser(newUser);});
		
		newUser.setUsername("veryunique");
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			userService.addUser(newUser);});
		
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
		user.setRole(new UserRole(2, "ROLE_ADMIN"));
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
		user.get().setRole(new UserRole(2, "ROLE_ADMIN"));
		user.get().setPassword("pass");
		
		return user;
	}

}
