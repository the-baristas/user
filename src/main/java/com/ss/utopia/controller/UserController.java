package com.ss.utopia.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.ss.utopia.entity.User;

import com.ss.utopia.service.UserService;



@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("{userId}")
	public User getUserById(@PathVariable("userId") Integer userId) throws ResponseStatusException {

		return userService.getUserById(userId);

	}

	@GetMapping("email/{email}")
	public User getUserByEmail(@PathVariable("email") String email) throws ResponseStatusException {

		return userService.getUserByEmail(email);

	}
	@GetMapping("username/{username}")
	public User getUserByUsername(@PathVariable("username") String username) throws ResponseStatusException{
		return userService.getUserByUsername(username);
	}
	

	@PostMapping("")
	//@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> createUser(@Valid @RequestBody User user, UriComponentsBuilder builder)
			throws ResponseStatusException {
		userService.addUser(user);
		return ResponseEntity.created(builder.path("/users/{userId}").build(user.getUserId())).build();
	}
	
	@PutMapping("{userId}")
	public ResponseEntity<String> updateUser(@PathVariable Integer userId, @RequestBody User user) throws ResponseStatusException {
		userService.updateUser(userId, user);
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	@DeleteMapping("{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer userId) throws ResponseStatusException {

		userService.deleteUserById(userId);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);

	}

}
