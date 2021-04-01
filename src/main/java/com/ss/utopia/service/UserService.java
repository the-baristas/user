package com.ss.utopia.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.ss.utopia.Utils;
import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.entity.User;
import com.ss.utopia.exception.EmailException;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;

	public User getUserById(Integer userId) {
		return userDAO.findById(userId).get();
	}

	public List<User> getAllUsers() {
		return userDAO.findAll();
	}

	public List<User> getUserByEmail(String email) throws EmailException {
		checkEmailValid(email);
		return userDAO.findByUserEmail(email);

	}

	public User addUser(User user) throws EmailException {
		checkEmailValid(user.getEmail());
		user.setPassword(Utils.passwordEncoder().encode(user.getPassword()));
		return userDAO.save(user);
	}

	public void updateUser(User user) throws EmailException {
		try {
			if(user.getEmail() != null)
				checkEmailValid(user.getEmail());
			userDAO.findById(user.getUserId()).get();
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		user.setPassword(Utils.passwordEncoder().encode(user.getPassword()));
		userDAO.save(user);
	}

	public void deleteUserById(Integer userId) {
		try {
			userDAO.deleteById(userId);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	private void checkEmailValid(String email) throws EmailException {
		Pattern pattern = Pattern.compile("^[a-z+[0-9]]{1,}[@][a-z+[0-9]]{1,}[\\.][a-z+[0-9]]{1,}$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		if(!matcher.find())
			throw new EmailException("Not a valid email address format");

	}
}
