package com.ss.utopia.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.ss.utopia.dto.UserDTO;
import com.ss.utopia.entity.RegistrationConfirmation;
import com.ss.utopia.entity.User;
import com.ss.utopia.exception.ConfirmationExpiredException;
import com.ss.utopia.login.jwt.JwtTokenVerifier;
import com.ss.utopia.service.RegistrationConfirmationService;
import com.ss.utopia.service.UserRoleService;
import com.ss.utopia.service.UserService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@RestController
@RequestMapping("/users")
@SecurityScheme(name = "bearer", // can be set to anything
		type = SecuritySchemeType.HTTP, scheme = "bearer")
@OpenAPIDefinition(info = @Info(title = "User Service", version = "v1"), security = @SecurityRequirement(name = "bearer"))
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private RegistrationConfirmationService confirmationService;

	@Value("${jwt.secretKey}")
	private String jwtSecretKey;

	@GetMapping("health")
	public String healthCheck() {
		return "ye";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("")
	public Page<UserDTO> getAllUsers(@RequestParam(name = "page") Integer page,
			@RequestParam(name = "size") Integer size,
			@RequestParam(name = "activeOnly", required = false) Boolean activeOnly) {
		if (activeOnly == null || !activeOnly) {
			// return all users, both active and inactive
			return userService.getAllUsers(page, size).map(user -> {
				return entityToDto(user);
			});
		} else {
			return userService.getAllUsersFilterByActive(page, size, activeOnly).map(user -> {
				return entityToDto(user);
			});
		}

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("search")
	public Page<UserDTO> findUsersBySearchTerm(@RequestParam("term") String searchTerm,
			@RequestParam("page") Integer page, @RequestParam("size") Integer size,
			@RequestParam(name = "activeOnly", required = false) Boolean activeOnly) {

		if (activeOnly == null || !activeOnly) {
			return userService
					.findAllUserBySearchTerm(searchTerm, page, size)
					.map(user -> {return entityToDto(user);});
		}
		else {
			return userService
					.findAllUserBySearchTermFilterByActive(searchTerm, page, size, activeOnly)
					.map(user -> {return entityToDto(user);});
		}
		
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_AGENT')")
	@GetMapping("{userId}")
	public UserDTO getUserById(@PathVariable("userId") Integer userId, @RequestHeader Map<String, String> header)
			throws ResponseStatusException {

		UserDTO userDto = entityToDto(userService.getUserById(userId));
		checkUsernameRequestMatchesResponse(header, userDto.getUsername());
		return userDto;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_AGENT')")
	@GetMapping("email/{email}")
	public UserDTO getUserByEmail(@PathVariable("email") String email, @RequestHeader Map<String, String> header)
			throws ResponseStatusException {

		UserDTO userDto = entityToDto(userService.getUserByEmail(email));
		checkUsernameRequestMatchesResponse(header, userDto.getUsername());
		return userDto;

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_AGENT')")
	@GetMapping("username/{username}")
	public UserDTO getUserByUsername(@PathVariable("username") String username,
			@RequestHeader Map<String, String> header) throws ResponseStatusException {
		UserDTO userDto = entityToDto(userService.getUserByUsername(username));
		checkUsernameRequestMatchesResponse(header, userDto.getUsername());
		return userDto;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_AGENT')")
	@GetMapping("phone/{phone}")
	public UserDTO getUserByPhoneNumber(@PathVariable("phone") String phone,
			@RequestHeader Map<String, String> header) {

		UserDTO userDto = entityToDto(userService.getUserByPhoneNumber(phone));
		checkUsernameRequestMatchesResponse(header, userDto.getUsername());
		return userDto;
	}

	@PostMapping("")
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDto, UriComponentsBuilder builder)
			throws ResponseStatusException {
		User user = dtoToEntity(userDto);
		UserDTO addedUser = entityToDto(userService.addUser(user));
		return ResponseEntity.status(HttpStatus.CREATED)
				.location(builder.path("/users/{userId}").buildAndExpand(user.getUserId()).toUri()).body(addedUser);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER')")
	@PutMapping("{userId}")
	public ResponseEntity<String> updateUser(@PathVariable Integer userId, @RequestBody UserDTO userDto,
			@RequestHeader Map<String, String> header) throws ResponseStatusException {
		User userToUpdate = userService.getUserById(userId);
		checkUsernameRequestMatchesResponse(header, userToUpdate.getUsername());

		userService.updateUser(userId, dtoToEntity(userDto));
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_AGENT')")
	@DeleteMapping("{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Integer userId, @RequestHeader Map<String, String> header)
			throws ResponseStatusException {
		UserDTO userDto = entityToDto(userService.getUserById(userId));
		checkUsernameRequestMatchesResponse(header, userDto.getUsername());
		userService.deleteUserById(userId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_AGENT')")
	@DeleteMapping("username/{username}")
	public ResponseEntity<String> deleteUserByUsername(@PathVariable String username,
			@RequestHeader Map<String, String> header) throws ResponseStatusException {
		UserDTO userDto = entityToDto(userService.getUserByUsername(username));
		checkUsernameRequestMatchesResponse(header, userDto.getUsername());
		userService.deleteUserById(userDto.getUserId());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("registration")
	public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserDTO userDto) {

		RegistrationConfirmation confirmation = userService.registerUser(dtoToEntity(userDto));

		Map<String, String> toReturn = new HashMap<String, String>();
		toReturn.put("token", confirmation.getToken());
		return ResponseEntity.status(HttpStatus.CREATED).body(toReturn);
	}

	@GetMapping("registration/{confirmationToken}")
	public String confirmRegistration(@PathVariable String confirmationToken, UriComponentsBuilder builder)
			throws ConfirmationExpiredException {

		RegistrationConfirmation confirmation = confirmationService.findByToken(confirmationToken);

		User user = userService.confirmRegistration(confirmation);
		return "Thank you " + user.getGivenName() + ". Your account is now verified.";
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
		JwtTokenVerifier tokenVerifier = new JwtTokenVerifier(jwtSecretKey);
		String username = tokenVerifier.getUsernameFromToken(header.get("authorization"));
		String role = new JwtTokenVerifier(jwtSecretKey).getRoleFromToken(header.get("authorization"));

		// if the user who sent the request is not an admin, then they can't view other
		// users' information
		// they can only view and alter their own
		if (!role.contains("ADMIN") && !username.equals(responseUsername)) {

			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"Only admins can access another user's information.");

		}
	}

}
