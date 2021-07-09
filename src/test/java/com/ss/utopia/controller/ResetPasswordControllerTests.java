package com.ss.utopia.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import com.ss.utopia.dto.ResetPasswordRequestDTO;
import com.ss.utopia.entity.ResetPasswordConfirmation;
import com.ss.utopia.entity.User;
import com.ss.utopia.entity.UserRole;
import com.ss.utopia.login.UtopiaUserDetailsService;
import com.ss.utopia.service.ResetPasswordService;
import com.ss.utopia.service.UserService;

@ExtendWith(SpringExtension.class)
@WithMockUser(authorities = { "ROLE_ADMIN" })
@WebMvcTest(ResetPasswordController.class)
@AutoConfigureMockMvc(addFilters=false)
public class ResetPasswordControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ResetPasswordService resetPasswordService;

	@MockBean
	private UserService userService;
	
	@MockBean
	private UtopiaUserDetailsService userDetailsService;
	
	private WebTestClient webTestClient;
	
	@BeforeEach
    void setUp() {
        webTestClient = MockMvcWebTestClient.bindTo(mockMvc).build();
    }
	
	@Test
	void testCreateResetPasswordRequest() throws Exception {
		ResetPasswordConfirmation confirmation = new ResetPasswordConfirmation();
		confirmation.setToken("token");
		User user = makeUser();
		when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
		when(resetPasswordService.handleResetPasswordRequest(user)).thenReturn(confirmation);
		
		String result = webTestClient.post().uri("/users/password/" + user.getEmail())
		    	.exchange().expectStatus().isCreated().expectBody().returnResult().toString();
				
		Assertions.assertTrue(result.contains(confirmation.getToken()));
				
	}
	
	@Test
	void testResetPassword() throws Exception {
		ResetPasswordRequestDTO request = new ResetPasswordRequestDTO();
		request.setPassword("pass");
		request.setToken("token");
		User user = makeUser();
		
		when(resetPasswordService.changePassword(request.getToken(), request.getPassword())).thenReturn(user);
		
		String result = webTestClient.put().uri("/users/password")
				.contentType(MediaType.APPLICATION_JSON).bodyValue(request)
		    	.exchange().expectStatus().isCreated().expectBody().returnResult().toString();
				
		Assertions.assertTrue(result.contains("Password changed"));
				
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
