package com.ss.utopia.controller;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.entity.User;
import com.ss.utopia.exception.EmailException;
import com.ss.utopia.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/utopia_airlines/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("")
	public List<User> getAllUser() {
		return userService.getAllUsers();
	}

	@GetMapping("{userId}")
	public User getUserById(@PathVariable("userId") Integer userId) {
		return userService.getUserById(userId);
	}

	@GetMapping("email/{email}")
	public List<User> getUserById(@PathVariable("email") String email) throws EmailException {

		return userService.getUserByEmail(email);

	}

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public User createUser(@Valid @RequestBody User user) throws EmailException {
		return userService.addUser(user);
	}

	@PutMapping("")
	public ResponseEntity<String> updateUser(@RequestBody User user) throws EmailException {
		try {
			userService.updateUser(user);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
		try {
			userService.deleteUserById(userId);
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}

}
