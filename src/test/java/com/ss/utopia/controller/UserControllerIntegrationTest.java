package com.ss.utopia.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.entity.User;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

	@Autowired
	private WebTestClient webClient;
	
	@Autowired
	private UserDAO userDao;
	
	@Test
	public void testFindUserById() {
		User user = makeUser();
		userDao.save(user);
		
		webClient.get().uri("/users/{userId}", user.getUserId())
			.accept(MediaType.APPLICATION_JSON)
			.exchange().expectStatus().isOk()
			.expectBody(User.class).isEqualTo(user);
	}
	
	@Test
	public void testFindUserByIdUserNotFound() {
		webClient.get().uri("/users/{userId}", 999)
		.accept(MediaType.APPLICATION_JSON)
		.exchange().expectStatus().isNotFound();
	}
	
	@Test
	public void testFindUserByEmail() {
		User user = makeUser();
		userDao.save(user);
		
		webClient.get().uri("/users/email/{email}", user.getEmail())
			.accept(MediaType.APPLICATION_JSON)
			.exchange().expectStatus().isOk()
			.expectBody(User.class).isEqualTo(user);
	}
	
	@Test
	public void testFindEmailByUserNotFound() {
		webClient.get().uri("/users/email/{email}", "doesnotexist@gmail.com")
		.accept(MediaType.APPLICATION_JSON)
		.exchange().expectStatus().isNotFound();
	}
	
	@Test
	public void testFindUserByUsername() {
		User user = makeUser();
		userDao.save(user);
		
		webClient.get().uri("/users/email/{email}", user.getEmail())
			.accept(MediaType.APPLICATION_JSON)
			.exchange().expectStatus().isOk()
			.expectBody(User.class).isEqualTo(user);
	}
	
	@Test
	public void testFindUsernameByUserNotFound() {
		webClient.get().uri("/users/username/{username}", "absolutelynot9001")
		.accept(MediaType.APPLICATION_JSON)
		.exchange().expectStatus().isNotFound();
	}
	
	@Test
	public void testUpdateUser() {
		User user = makeUser();
		userDao.save(user);
		
		webClient.get().uri("/users/{userId}", user.getUserId())
		.accept(MediaType.APPLICATION_JSON)
		.exchange().expectStatus().isOk()
		.expectBody(User.class).isEqualTo(user);
		
		user.setGivenName("New");
		userDao.save(user);
		
		webClient.get().uri("/users/{userId}", user.getUserId())
		.accept(MediaType.APPLICATION_JSON)
		.exchange().expectStatus().isOk()
		.expectBody(User.class).isEqualTo(user);
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
