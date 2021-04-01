package com.ss.utopia.service;

import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.ss.utopia.controller.UserController;
import com.ss.utopia.entity.User;
import com.ss.utopia.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserServiceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserController controller;
	
	@MockBean
	private UserService userService;
	
	
	@Test
	public void controllerLoads() throws Exception{
		assertTrue(controller != null);
	}
	
	@Test 
	void testGetUserById() throws Exception{
		User user = userService.getUserById(1);


		when(userService.getUserById(1)).thenReturn(user);
		
		mockMvc.perform(get("/utopia_airlines/user/1")
		.contentType(MediaType.APPLICATION_JSON))
		//.andExpect(jsonPath("$.phone").value(user.getPhone()) )
		.andExpect(status().isOk());
	}
	
	@Test
	void testGetAll() throws Exception{
		List<User> users = new ArrayList<User>();
		User user1 = new User();
		user1.setUserId(1);
		user1.setGivenName("First");
		user1.setFamilyName("Last");
		user1.setEmail("aa@email.com");
		user1.setIsActive(true);
		user1.setPhone("1111111111");
		user1.setRole("admin");
		user1.setPassword("pass");
		User user2 = new User();
		user2.setUserId(2);
		user2.setGivenName("First");
		user2.setFamilyName("Last");
		user2.setEmail("bb@email.com");
		user2.setIsActive(true);
		user2.setPhone("8195678900");
		user2.setRole("admin");
		user2.setPassword("pass");
		users.add(user1);
		users.add(user2);
		
		when(userService.getAllUsers()).thenReturn(users);
		
		mockMvc.perform(get("/utopia_airlines/user")
		.contentType(MediaType.APPLICATION_JSON))
		//.andExpect(jsonPath("$.phone").value(user.getPhone()) )
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(2)));
	}

}
