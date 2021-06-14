package com.ss.utopia.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.utopia.dto.UserDTO;
import com.ss.utopia.entity.User;
import com.ss.utopia.entity.UserRole;
import com.ss.utopia.login.UtopiaUserDetailsService;
import com.ss.utopia.login.jwt.JwtUtils;
import com.ss.utopia.service.UserRoleService;
import com.ss.utopia.service.UserService;

import io.jsonwebtoken.Jwts;

@ExtendWith(SpringExtension.class)
@WithMockUser(authorities = { "ROLE_ADMIN" })
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
	
	@Value("${jwt.secretKey}")
    private String jwtSecretKey;
	
	private WebTestClient webTestClient;
	
	@BeforeEach
    void setUp() {
        webTestClient = MockMvcWebTestClient.bindTo(mockMvc).build();
    }

	@Test
	void controllerLoads() throws Exception {
		assertNotNull(controller);
	}

	@Test
	void testGetUserById() throws Exception {
		User user = makeUser();
		UserDTO userDto = makeUserDTO();

		//Build tokens
		String adminToken = Jwts.builder().setSubject("someUsername23")
			.claim("authorities", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
			.setIssuedAt(new Date())
			.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(JwtUtils.getTokenExpirationAfterDays())))
			.signWith(JwtUtils.getSecretKey(jwtSecretKey))
			.compact();
		
		when(userService.getUserById(1)).thenReturn(user);

		mockMvc.perform(get("/users/1").header("authorization", adminToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.userId").value(userDto.getUserId()))
				.andExpect(jsonPath("$.givenName").value(userDto.getGivenName()))
				.andExpect(jsonPath("$.familyName").value(userDto.getFamilyName()))
				.andExpect(jsonPath("$.email").value(userDto.getEmail()))
				.andExpect(jsonPath("$.role").value(userDto.getRole()))
				.andExpect(jsonPath("$.phone").value(userDto.getPhone())).andExpect(status().isOk());
	}
	
	@Test
	void testGetUserByIdThrowsExceptionWhenRequestUsernameDoesNotMatchResponseUsername() throws Exception {
		User user = makeUser();
		UserDTO userDto = makeUserDTO();
		userDto.setRole("ROLE_CUSTOMER");

		//Build tokens
		String adminToken = Jwts.builder().setSubject("differentUsername")
			.claim("authorities", Arrays.asList(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
			.setIssuedAt(new Date())
			.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(JwtUtils.getTokenExpirationAfterDays())))
			.signWith(JwtUtils.getSecretKey(jwtSecretKey))
			.compact();
		
		when(userService.getUserById(1)).thenReturn(user);

		mockMvc.perform(get("/users/1").header("authorization", adminToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}
	
	@Test
	void testGetUserByEmail() throws Exception {
		User user = makeUser();
		UserDTO userDto = makeUserDTO();
		
		//Build tokens
		String adminToken = Jwts.builder().setSubject("someUsername23")
			.claim("authorities", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
			.setIssuedAt(new Date())
			.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(JwtUtils.getTokenExpirationAfterDays())))
			.signWith(JwtUtils.getSecretKey(jwtSecretKey))
			.compact();

		when(userService.getUserByEmail("username@email.org")).thenReturn(user);

		mockMvc.perform(get("/users/email/username@email.org").header("authorization", adminToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.userId").value(userDto.getUserId()))
				.andExpect(jsonPath("$.givenName").value(userDto.getGivenName()))
				.andExpect(jsonPath("$.familyName").value(userDto.getFamilyName()))
				.andExpect(jsonPath("$.email").value(userDto.getEmail()))
				.andExpect(jsonPath("$.role").value(userDto.getRole()))
				.andExpect(jsonPath("$.phone").value(userDto.getPhone())).andExpect(status().isOk());
	}
	
	@Test
	void testGetUserByUsername() throws Exception {
		User user = makeUser();
		UserDTO userDto = makeUserDTO();
		
		//Build tokens
		String adminToken = Jwts.builder().setSubject("someUsername23")
			.claim("authorities", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
			.setIssuedAt(new Date())
			.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(JwtUtils.getTokenExpirationAfterDays())))
			.signWith(JwtUtils.getSecretKey(jwtSecretKey))
			.compact();

		when(userService.getUserByUsername("someUsername23")).thenReturn(user);

		mockMvc.perform(get("/users/username/someUsername23").header("authorization", adminToken).contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.userId").value(userDto.getUserId()))
				.andExpect(jsonPath("$.givenName").value(userDto.getGivenName()))
				.andExpect(jsonPath("$.familyName").value(userDto.getFamilyName()))
				.andExpect(jsonPath("$.email").value(userDto.getEmail()))
				.andExpect(jsonPath("$.role").value(userDto.getRole()))
				.andExpect(jsonPath("$.phone").value(userDto.getPhone())).andExpect(status().isOk());
	}
	
	@Test
	void testGetUserByPhone() throws Exception {
		User user = makeUser();
		UserDTO userDto = makeUserDTO();
		
		//Build tokens
		String adminToken = Jwts.builder().setSubject("someUsername23")
			.claim("authorities", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
			.setIssuedAt(new Date())
			.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(JwtUtils.getTokenExpirationAfterDays())))
			.signWith(JwtUtils.getSecretKey(jwtSecretKey))
			.compact();

		when(userService.getUserByPhoneNumber("1111111111")).thenReturn(user);

		mockMvc.perform(get("/users/phone/1111111111").header("authorization", adminToken).contentType(MediaType.APPLICATION_JSON))
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
		
		Page<User> userPage = new PageImpl<User>(users);
				
		when(userService.getAllUsers(0, 2)).thenReturn(userPage);

		mockMvc.perform(get("/users?page=0&size=2").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(2)));
	}

	@Test
	void testAddUser() throws Exception {
		User user = makeUser();
		UserDTO userDto = makeUserDTO();
		userDto.setPassword("password");
		
		when(userService.addUser(controller.dtoToEntity(userDto))).thenReturn(user);
		
	    webTestClient.post().uri("/users")
        	.contentType(MediaType.APPLICATION_JSON).bodyValue("{\"userId\":1,\"givenName\":\"First\",\"familyName\":\"Last\",\"password\":\"password\",\"username\":\"someUsername23\",\"email\":\"username@email.org\",\"phone\":\"1111111111\",\"role\":\"ROLE_ADMIN\",\"active\":true}")
        	.exchange().expectStatus().isCreated().expectHeader()
        	.contentType(MediaType.APPLICATION_JSON);
	}
 
	@Test
	void testUpdateUser() throws Exception {
		User user = makeUser();
		UserDTO userDto = makeUserDTO();
		
		//Build tokens
		String adminToken = Jwts.builder().setSubject("someUsername23")
			.claim("authorities", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
			.setIssuedAt(new Date())
			.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(JwtUtils.getTokenExpirationAfterDays())))
			.signWith(JwtUtils.getSecretKey(jwtSecretKey))
			.compact();

		when(userService.getUserById(userDto.getUserId())).thenReturn(user);

		when(userService.updateUser(userDto.getUserId(), controller.dtoToEntity(userDto))).thenReturn(user);
		mockMvc.perform(put("/users/{userId}", userDto.getUserId(), userDto).header("authorization", adminToken).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDto))).andExpect(status().isOk());

	}

	@Test
	void testDeleteUser() throws Exception {
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
