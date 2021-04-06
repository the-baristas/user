package com.ss.utopia.controller;

import static org.hamcrest.Matchers.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.entity.User;
import com.ss.utopia.exception.UserNotFoundException;
import com.ss.utopia.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserController controller;

	@MockBean
	private UserService userService;

	@Test
	public void controllerLoads() throws Exception {
		assertTrue(controller != null);
	}

	@Test
	void testGetUserById() throws Exception {
		User user = makeUser();

		when(userService.getUserById(1)).thenReturn(user);

		mockMvc.perform(get("/utopia_airlines/user/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.userId").value(user.getUserId()))
				.andExpect(jsonPath("$.givenName").value(user.getGivenName()))
				.andExpect(jsonPath("$.familyName").value(user.getFamilyName()))
				.andExpect(jsonPath("$.email").value(user.getEmail()))
				.andExpect(jsonPath("$.password").value(user.getPassword()))
				.andExpect(jsonPath("$.role").value(user.getRole()))
				.andExpect(jsonPath("$.isActive").value(user.getIsActive()))
				.andExpect(jsonPath("$.phone").value(user.getPhone())).andExpect(status().isOk());
	}
	
	@Test
	void testGetUserByEmail() throws Exception {
		User user = makeUser();

		when(userService.getUserByEmail("username@email.org")).thenReturn(user);

		mockMvc.perform(get("/utopia_airlines/user/email/username@email.org").contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.userId").value(user.getUserId()))
				.andExpect(jsonPath("$.givenName").value(user.getGivenName()))
				.andExpect(jsonPath("$.familyName").value(user.getFamilyName()))
				.andExpect(jsonPath("$.email").value(user.getEmail()))
				.andExpect(jsonPath("$.password").value(user.getPassword()))
				.andExpect(jsonPath("$.role").value(user.getRole()))
				.andExpect(jsonPath("$.isActive").value(user.getIsActive()))
				.andExpect(jsonPath("$.phone").value(user.getPhone())).andExpect(status().isOk());
	}

	@Test
	void testUserGetByIdThrowsUserNotFoundException() throws Exception {
		when(userService.getUserById(99)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Could not find user with id = " + 99));

		mockMvc.perform(get("/utopia_airlines/user/99")).andExpect(status().isNotFound());
	}

	@Test
	void testGetAll() throws Exception {
		List<User> users = new ArrayList<User>();
		User user1 = makeUser();
		User user2 = makeUser();
		user2.setUserId(2);
		user2.setEmail("bb@gmail.com");
		user2.setPhone("8195678900");
		users.add(user1);
		users.add(user2);

		when(userService.getAllUsers()).thenReturn(users);

		mockMvc.perform(get("/utopia_airlines/user").contentType(MediaType.APPLICATION_JSON))
				// .andExpect(jsonPath("$.phone").value(user.getPhone()) )
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void testAddUser() throws Exception {
		User user = makeUser();

		when(userService.addUser(user)).thenReturn(user);

		mockMvc.perform(post("/utopia_airlines/user").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(user)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().exists("Location"))
				.andExpect(MockMvcResultMatchers.header().string("Location", Matchers.containsString("1")));

		verify(userService).addUser(user);
	}

	@Test
	public void testUpdateUser() throws Exception {
		User user = makeUser();

		when(userService.getUserById(user.getUserId())).thenReturn(user);

		when(userService.updateUser(user)).thenReturn(user);
		mockMvc.perform(put("/utopia_airlines/user", user).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(user))).andExpect(status().isOk());

		// verify(userService, times(1)).getUserById(user.getUserId());
		// verify(userService, times(1)).updateUser(user);
		// verifyNoMoreInteractions(userService);
	}

	@Test
	public void testDeleteUser() throws Exception {
		User user = makeUser();

		when(userService.getUserById(user.getUserId())).thenReturn(user);

		doNothing().when(userService).deleteUserById(user.getUserId());
		mockMvc.perform(delete("/utopia_airlines/user/{userId}", user.getUserId())).andExpect(status().isNoContent());

		verify(userService, times(1)).deleteUserById(user.getUserId());
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	void testDeleteUserhrowsUserNotFoundException() throws Exception {

		doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Could not find user with id = " + 99)).when(userService).deleteUserById(99);
		mockMvc.perform(delete("/utopia_airlines/user/99")).andExpect(status().isNotFound());
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
