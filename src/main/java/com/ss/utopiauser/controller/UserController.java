package com.ss.utopiauser.controller;

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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ss.utopiauser.entity.User;
import com.ss.utopiauser.service.UserService;

@RestController
@RequestMapping("/utopia_airlines")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/user")
	public List<User> getAllUser(){
		return userService.getAllUsers();
	}
	@GetMapping("/user/{userId}")
	public User getUserById(@PathVariable("userId") Integer userId) {
		return userService.getUserById(userId);
	}
	
	@PostMapping("/user")
	@ResponseStatus(HttpStatus.CREATED)
	public User createUser(@Valid @RequestBody User user) {
		return userService.addUser(user);
	}
	
	@PutMapping("/user")
	public ResponseEntity<String> updateUser(@RequestBody User user){
		try {
			userService.updateUser(user);
			return new ResponseEntity<String>(HttpStatus.OK);
		}catch(NoSuchElementException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer userId){
		try{
			userService.deleteUserById(userId);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch(RuntimeException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	
}
