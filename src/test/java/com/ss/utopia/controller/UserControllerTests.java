package com.ss.utopia.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.converter.UserConverter;
import com.ss.utopia.dto.UserDTO;
import com.ss.utopia.entity.User;
import com.ss.utopia.entity.UserRole;
import com.ss.utopia.login.UtopiaUserDetailsService;
import com.ss.utopia.service.UserRoleService;
import com.ss.utopia.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters=false)
class UserControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserController controller;

	@MockBean
	private UserService userService;
	
	@MockBean
	private UserRoleService userRoleService;
	
	@MockBean
	private UtopiaUserDetailsService userDetailsService;

	@Test
	public void controllerLoads() throws Exception {
		assertTrue(controller != null);
	}

	@Test
	void testGetUserById() throws Exception {
		User user = makeUser();
		UserDTO userDto = makeUserDTO();
		UserRole role = new UserRole(2, "ROLE_ADMIN");

		
		when(userService.getUserById(1)).thenReturn(user);

		mockMvc.perform(get("/users/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.userId").value(userDto.getUserId()))
				.andExpect(jsonPath("$.givenName").value(userDto.getGivenName()))
				.andExpect(jsonPath("$.familyName").value(userDto.getFamilyName()))
				.andExpect(jsonPath("$.email").value(userDto.getEmail()))
				.andExpect(jsonPath("$.role").value(userDto.getRole()))
				.andExpect(jsonPath("$.phone").value(userDto.getPhone())).andExpect(status().isOk());
	}
	
	@Test
	void testGetUserByEmail() throws Exception {
		User user = makeUser();
		UserDTO userDto = makeUserDTO();

		when(userService.getUserByEmail("username@email.org")).thenReturn(user);

		mockMvc.perform(get("/users/email/username@email.org").contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.userId").value(userDto.getUserId()))
				.andExpect(jsonPath("$.givenName").value(userDto.getGivenName()))
				.andExpect(jsonPath("$.familyName").value(userDto.getFamilyName()))
				.andExpect(jsonPath("$.email").value(userDto.getEmail()))
				.andExpect(jsonPath("$.role").value(userDto.getRole()))
				.andExpect(jsonPath("$.phone").value(userDto.getPhone())).andExpect(status().isOk());
	}

	@Test
	void testUserGetByIdThrowsUserNotFoundException() throws Exception {
		when(userService.getUserById(99)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Could not find user with id = " + 99));

		mockMvc.perform(get("/users/99")).andExpect(status().isNotFound());
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
		
		List<UserDTO> userDtos = new ArrayList<UserDTO>();
		UserDTO userDto1 = makeUserDTO();
		UserDTO userDto2 = makeUserDTO();
		userDto2.setUserId(2);
		userDto2.setEmail("bb@gmail.com");
		userDto2.setPhone("8195678900");
		userDtos.add(userDto1);
		userDtos.add(userDto2);
				
		when(userService.getAllUsers()).thenReturn(users);

		mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	public void testAddUser() throws Exception {
		User user = makeUser();
		UserDTO userDto = makeUserDTO();
		
		when(userService.addUser(controller.dtoToEntity(userDto))).thenReturn(user);

		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(controller.dtoToEntity(userDto))))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().exists("Location"))
				.andExpect(MockMvcResultMatchers.header().string("Location", Matchers.containsString("1")));

		verify(userService).addUser(UserConverter.dtoToEntity(userDto));
	}

	@Test
	public void testUpdateUser() throws Exception {
		User user = makeUser();
		UserDTO userDto = makeUserDTO();

		when(userService.getUserById(userDto.getUserId())).thenReturn(user);

		when(userService.updateUser(userDto.getUserId(), controller.dtoToEntity(userDto))).thenReturn(UserConverter.dtoToEntity(userDto));
		mockMvc.perform(put("/users/{userId}", userDto.getUserId(), userDto).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDto))).andExpect(status().isOk());

	}

	@Test
	public void testDeleteUser() throws Exception {
		User user = makeUser();
		UserDTO userDto = makeUserDTO();
		
		when(userService.getUserById(userDto.getUserId())).thenReturn(user);

		doNothing().when(userService).deleteUserById(userDto.getUserId());
		mockMvc.perform(delete("/users/{userId}", userDto.getUserId())).andExpect(status().isNoContent());

		verify(userService, times(1)).deleteUserById(userDto.getUserId());
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	void testDeleteUserhrowsUserNotFoundException() throws Exception {

		doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Could not find user with id = " + 99)).when(userService).deleteUserById(99);
		mockMvc.perform(delete("/users/99")).andExpect(status().isNotFound());
	}

	private UserDTO makeUserDTO() {
		return controller.entityToDto(makeUser());
	}
	
	private User makeUser() {
		User user = new User();
		user.setUserId(1);
		user.setGivenName("First");
		user.setFamilyName("Last");
		user.setUsername("someUsername23");
		user.setEmail("username@email.org");
		user.setActive(true);
		user.setPhone("1111111111");
		user.setRole(new UserRole(2, "ROLE_ADMIN"));
		user.setPassword("pass");
		return user;
	}
}
