package com.ss.utopia.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.entity.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerIntegrationTestDelete {

	@Autowired
	private WebTestClient webClient;
	
	@Autowired
	private UserDAO userDao;
	
	@Test
	public void testDeleteUser() {
		User user = makeUser();
		userDao.save(user);
		
		webClient.get().uri("/utopia_airlines/user/{userId}", user.getUserId())
		.accept(MediaType.APPLICATION_JSON)
		.exchange().expectStatus().isOk()
		.expectBody(User.class).isEqualTo(user);
		
		//userDao.delete(user);
		
		webClient.delete().uri("/utopia_airlines/user/{userId}", user.getUserId())
		.accept(MediaType.APPLICATION_JSON)
		.exchange().expectStatus().isNoContent();
	}
	
	@Test
	public void testDeleteUserNotFound() {
		webClient.delete().uri("/utopia_airlines/user/{userId}", 9001)
		.accept(MediaType.APPLICATION_JSON)
		.exchange().expectStatus().isNotFound();
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
