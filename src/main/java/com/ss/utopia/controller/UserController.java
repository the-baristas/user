package com.ss.utopia.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.ss.utopia.converter.UserConverter;
import com.ss.utopia.dto.UserDTO;
import com.ss.utopia.entity.User;
import com.ss.utopia.service.UserService;



@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("")
	public List<UserDTO> getAllUsers() {
		List<User> users = userService.getAllUsers();
		List<UserDTO> userDtos = users.stream().map((user) -> {return UserConverter.entityToDto(user);})
				.collect(Collectors.toList());
		return userDtos;
	}

	@GetMapping("{userId}")
	public UserDTO getUserById(@PathVariable("userId") Integer userId) throws ResponseStatusException {
		UserDTO userDto = UserConverter.entityToDto(userService.getUserById(userId));
		return userDto;

	}


	@GetMapping("email/{email}")
	public UserDTO getUserByEmail(@PathVariable("email") String email) throws ResponseStatusException {
		
		return UserConverter.entityToDto(userService.getUserByEmail(email));

	}
	@GetMapping("username/{username}")
	public UserDTO getUserByUsername(@PathVariable("username") String username) throws ResponseStatusException{
		
		return UserConverter.entityToDto(userService.getUserByUsername(username));
	}
	

	@PostMapping("")
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDto, UriComponentsBuilder builder)
			throws ResponseStatusException {
		
		User user = UserConverter.dtoToEntity(userDto);
		
		UserDTO addedUser = UserConverter.entityToDto(userService.addUser(user));
		return ResponseEntity.status(HttpStatus.CREATED)
				.location(builder.path("/users/{userId}").buildAndExpand(user.getUserId()).toUri()).body(addedUser);
	}
	
	@PutMapping("{userId}")
	public ResponseEntity<String> updateUser(@PathVariable Integer userId, @RequestBody UserDTO userDto) throws ResponseStatusException {
		User user = UserConverter.dtoToEntity(userDto);
		userService.updateUser(userId, user);
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	@DeleteMapping("{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer userId) throws ResponseStatusException {

		userService.deleteUserById(userId);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);

	}

}
