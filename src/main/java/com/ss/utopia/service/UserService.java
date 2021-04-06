package com.ss.utopia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ss.utopia.Utils;
import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.entity.User;


@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;

	public User getUserById(Integer userId) throws ResponseStatusException {
		return userDAO.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Could not find user with id = " + userId));
	}

	public List<User> getAllUsers() {
		return userDAO.findAll();
	}

	public User getUserByEmail(String email) throws ResponseStatusException {
		Utils.checkEmailValid(email);
		try {
		return userDAO.findByUserEmail(email).get(0);
		}
		catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Could not find user with email = " + email);
		}

	}

	public User addUser(User user) throws ResponseStatusException {
		Utils.checkEmailValid(user.getEmail());
		user.setPassword(Utils.passwordEncoder().encode(user.getPassword()));
		
		checkNoDuplicateFields(user);
		
		return userDAO.save(user);
	}

	public User updateUser(User user) throws ResponseStatusException {
			if(user.getEmail() != null)
				Utils.checkEmailValid(user.getEmail());
			userDAO.findById(user.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Could not find user with id = " + user.getUserId()));
			
		user.setPassword(Utils.passwordEncoder().encode(user.getPassword()));
		
		return userDAO.save(user);
	}

	public void deleteUserById(Integer userId) throws ResponseStatusException {
			userDAO.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Could not find user with id = " + userId));;
			userDAO.deleteById(userId);
	}
	
	private void checkNoDuplicateFields(User newUser) throws ResponseStatusException {
		List<User> users = userDAO.findAll();
		for(User user : users) {
			if(user.getEmail().equals(newUser.getEmail()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"A user with this email already exists");
			
			if(user.getUsername().equals(newUser.getUsername()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"A user with this username already exists");
			
			if(user.getPhone().equals(newUser.getPhone()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"A user with this phone number already exists");
		}
		
	}
}
