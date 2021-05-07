package com.ss.utopia.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.ss.utopia.converter.UserConverter;
import com.ss.utopia.dto.UserDTO;
import com.ss.utopia.entity.User;
import com.ss.utopia.login.jwt.JwtTokenVerifier;
import com.ss.utopia.service.UserRoleService;
import com.ss.utopia.service.UserService;



@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRoleService userRoleService;
	
	@GetMapping("")
	public Page<UserDTO> getAllUsers(@RequestParam(name="page") Integer page, @RequestParam(name="size") Integer size){
		Page<User> userPage = userService.getAllUsers(page, size);
		Page<UserDTO> userDtoPage = userPage.map((user) -> {return entityToDto(user);});
		return userDtoPage;
	}
	

	@GetMapping("{userId}")
	public UserDTO getUserById(@PathVariable("userId") Integer userId,
			@RequestHeader Map<String,String> header) throws ResponseStatusException {
		
		UserDTO userDto = entityToDto(userService.getUserById(userId));
		checkUsernameRequestMatchesResponse(header, userDto.getUsername());
		return userDto;
	}

	@GetMapping("email/{email}")
	public UserDTO getUserByEmail(@PathVariable("email") String email,
			 @RequestHeader Map<String,String> header) throws ResponseStatusException {
		
		UserDTO userDto = entityToDto(userService.getUserByEmail(email));
		checkUsernameRequestMatchesResponse(header, userDto.getUsername());
		return userDto;

	}
	
	@GetMapping("username/{username}")
	public UserDTO getUserByUsername(@PathVariable("username") String username,
			 @RequestHeader Map<String,String> header) throws ResponseStatusException{
		UserDTO userDto = entityToDto(userService.getUserByUsername(username));
		checkUsernameRequestMatchesResponse(header, userDto.getUsername());
		return userDto;
	}
	
	@GetMapping("phone/{phone}")
	public UserDTO getUserByPhoneNumber(@PathVariable("phone") String phone,
			 @RequestHeader Map<String,String> header) {

		UserDTO userDto = entityToDto(userService.getUserByPhoneNumber(phone));
		checkUsernameRequestMatchesResponse(header, userDto.getUsername());
		return userDto;
	}
	

	@PostMapping("")
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDto, UriComponentsBuilder builder)
			throws ResponseStatusException {
		
		User user = dtoToEntity(userDto);		
		UserDTO addedUser = entityToDto(userService.addUser(user));
		return ResponseEntity.status(HttpStatus.CREATED)
				.location(builder.path("/users/{userId}").buildAndExpand(user.getUserId()).toUri()).body(addedUser);
	}
	
	@PutMapping("{userId}")
	public ResponseEntity<String> updateUser(@PathVariable Integer userId, @RequestBody UserDTO userDto) throws ResponseStatusException {
		User user = dtoToEntity(userDto);
		userService.updateUser(userId, user);
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	@DeleteMapping("{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer userId) throws ResponseStatusException {

		userService.deleteUserById(userId);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);

	}
	
	public UserDTO entityToDto(User user) {
		ModelMapper mapper = new ModelMapper();
		UserDTO userDto = mapper.map(user, UserDTO.class);
		userDto.setRole(user.getRole().getRoleName());
		return userDto;
	}
	
	public User dtoToEntity(UserDTO userDto) {
		ModelMapper mapper = new ModelMapper();
		User user = mapper.map(userDto, User.class);
		user.setRole(userRoleService.getUserRoleByRoleName(userDto.getRole()));
		return user;
	}
	
	public void checkUsernameRequestMatchesResponse(Map<String, String> header, String responseUsername) {
		JwtTokenVerifier tokenVerifier = new JwtTokenVerifier();
		String username = tokenVerifier.getUsernameFromToken(header.get("authorization"));
		String role = new JwtTokenVerifier().getRoleFromToken(header.get("authorization"));
		
		//if the user who sent the request is an admin, then they other users' information
		if(!role.contains("ADMIN")) {
			//otherwise, the username of the request must match the username of the response
			if(!username.equals(responseUsername)) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can access another user's information.");

			}
		}
	}

}
