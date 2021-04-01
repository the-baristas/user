package com.ss.utopiauser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.ss.utopia.controller.UserController;
import com.ss.utopia.entity.User;
import com.ss.utopia.service.UserService;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserServiceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserController userController;
	
	@MockBean
	private UserService userService;
	
	
	@Test 
	void testGetUserById() throws Exception{
		User user = userService.getUserById(1);
		

		when(userService.getUserById(anyInt())).thenReturn(user);
		
		mockMvc.perform(get("/utopia_airlines/user/1"))
		.andExpect(jsonPath("$.phone").value(user.getPhone()) )
		.andExpect(status().isOk());
	}
	
//	@Test
//	void testGetAll() throws Exception{
//		
//		RequestBuilder request = MockMvcRequestBuilders.get("/utopia_airline/user");
//		MvcResult result = mockMvc.perform(request).andReturn();
//		assertEquals(userService.getAllUsers(), result.getResponse());
//	}

}
