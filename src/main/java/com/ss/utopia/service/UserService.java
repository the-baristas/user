package com.ss.utopia.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.ss.utopia.Utils;
import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.entity.User;
import com.ss.utopia.exception.EmailException;
import com.ss.utopia.exception.UserNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;

	public User getUserById(Integer userId) throws UserNotFoundException {
		return userDAO.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	public List<User> getAllUsers() {
		return userDAO.findAll();
	}

	public User getUserByEmail(String email) throws EmailException, UserNotFoundException {
		Utils.checkEmailValid(email);
		try {
		return userDAO.findByUserEmail(email).get(0);
		}
		catch(Exception e) {
			throw new UserNotFoundException("User not found");
		}

	}

	public User addUser(User user) throws EmailException {
		Utils.checkEmailValid(user.getEmail());
		user.setPassword(Utils.passwordEncoder().encode(user.getPassword()));
		return userDAO.save(user);
	}

	public User updateUser(User user) throws EmailException, UserNotFoundException {
			if(user.getEmail() != null)
				Utils.checkEmailValid(user.getEmail());
			userDAO.findById(user.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));;
			
		user.setPassword(Utils.passwordEncoder().encode(user.getPassword()));
		
		return userDAO.save(user);
	}

	public void deleteUserById(Integer userId) throws UserNotFoundException {
			userDAO.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));;
			userDAO.deleteById(userId);
	}
}
