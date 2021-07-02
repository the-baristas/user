package com.ss.utopia.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ss.utopia.Utils;
import com.ss.utopia.dao.RegistrationConfirmationDAO;
import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.email.EmailSender;
import com.ss.utopia.entity.RegistrationConfirmation;
import com.ss.utopia.entity.User;
import com.ss.utopia.exception.ConfirmationExpiredException;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	RegistrationConfirmationDAO confirmationDAO;
	
	@Autowired
	EmailSender emailSender;
	
	private Integer confirmationExpirationMinutes = 15;

	public User getUserById(Integer userId) throws ResponseStatusException {
		return userDAO.findById(userId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with id = " + userId));
	}
	
	public Page<User> getAllUsers(Integer page, Integer size){
		return userDAO.findAll(PageRequest.of(page, size));
	}
	
	public Page<User> findAllUserBySearchTerm(String searchTerm, Integer page, Integer size){
		return userDAO.findDistinctBySearchTerm(searchTerm, PageRequest.of(page, size));
	}

	public User getUserByEmail(String email) throws ResponseStatusException {
		Utils.checkEmailValid(email);
		try {
			return userDAO.findByUserEmail(email).get(0);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with email = " + email);
		}

	}

	public User getUserByUsername(String username) {
		try {
			return userDAO.findByUsername(username).get(0);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with username = " + username);
		}
	}
	
	public User getUserByPhoneNumber(String phone) {
		try {
			return userDAO.findByPhoneNumber(phone).get(0);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with phone number = " + phone);
		}
	}

	public User addUser(User user) throws ResponseStatusException {

		Utils.checkEmailValid(user.getEmail());
		Utils.checkPhoneNumberValid(user.getPhone());

		user.setPassword(Utils.passwordEncoder().encode(user.getPassword()));

		checkNoDuplicateFields(user);

		return userDAO.save(user);
	}

	public User updateUser(int userId, User newUserInfo) throws ResponseStatusException {
		User oldUserInfo = userDAO.findById(userId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with id = " + userId));

		Utils.checkEmailValid(newUserInfo.getEmail());
		
		if (newUserInfo.getPassword() != null)
			newUserInfo.setPassword(Utils.passwordEncoder().encode(newUserInfo.getPassword()));
		else
			newUserInfo.setPassword(oldUserInfo.getPassword());
		
		newUserInfo.setUserId(userId);
		checkNoDuplicateFields(newUserInfo);
		return userDAO.save(newUserInfo);
	}

	public void deleteUserById(Integer userId) throws ResponseStatusException {
		User deleted = userDAO.findById(userId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with id = " + userId));
		userDAO.deleteById(deleted.getUserId());
	}
	
	@Transactional
	public RegistrationConfirmation registerUser(User user) throws ResponseStatusException {
		
		RegistrationConfirmation confirmation = new RegistrationConfirmation(
		UUID.randomUUID().toString(),
		LocalDateTime.now(),
		LocalDateTime.now().plusMinutes(confirmationExpirationMinutes),
		user
		);	
		
		user.setActive(false);
		addUser(user);
		
		RegistrationConfirmation saved = confirmationDAO.save(confirmation);
		
		emailSender.sendEmail(user, saved);
		
		return saved;
	}
	

	public User confirmRegistration(RegistrationConfirmation confirmation ) throws ConfirmationExpiredException {
		User user = confirmation.getUser();
		
		LocalDateTime currentTime = LocalDateTime.now();
		if(currentTime.isAfter(confirmation.getExpiresAt())) {
			
			RegistrationConfirmation confirmationRetry = new RegistrationConfirmation(
					UUID.randomUUID().toString(),
					LocalDateTime.now(),
					LocalDateTime.now().plusMinutes(confirmationExpirationMinutes),
					user
					);	
					
					RegistrationConfirmation saved = confirmationDAO.save(confirmationRetry);
					emailSender.sendEmail(user, saved);
			throw new ConfirmationExpiredException(user.getEmail());
		}
		
		confirmation.setConfirmedAt(currentTime);
		confirmationDAO.save(confirmation);
		
		user.setActive(true);
		userDAO.save(user);
		
		return user;
	}

	//TODO: REFACTOR
	private void checkNoDuplicateFields(User newUser) throws ResponseStatusException {
		List<User> users = userDAO.findAll();
		for (User user : users) {
			if (user.equals(newUser))
				continue;
			if (user.getEmail().equals(newUser.getEmail()))
				throw new ResponseStatusException(HttpStatus.CONFLICT, "A user with this email already exists");

			if (user.getUsername().equals(newUser.getUsername()))
				throw new ResponseStatusException(HttpStatus.CONFLICT, "A user with this username already exists");

			if (user.getPhone().equals(newUser.getPhone()))
				throw new ResponseStatusException(HttpStatus.CONFLICT, "A user with this phone number already exists");
		}

	}


}
