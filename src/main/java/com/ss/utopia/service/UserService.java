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
		return userDAO.findById(userId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with id = " + userId));
	}

	public List<User> getAllUsers() {
		return userDAO.findAll();
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

	public User addUser(User user) throws ResponseStatusException {
		checkUserFieldsFilled(user);

		Utils.checkEmailValid(user.getEmail());
		Utils.checkPhoneNumberValid(user.getPhone());

		user.setPassword(Utils.passwordEncoder().encode(user.getPassword()));

		checkNoDuplicateFields(user);

		return userDAO.save(user);
	}

	public User updateUser(int userId, User newUserInfo) throws ResponseStatusException {
		User oldUserInfo = userDAO.findById(userId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with id = " + userId));

		if (newUserInfo.getEmail() != null)
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
		userDAO.findById(userId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user with id = " + userId));
		;
		userDAO.deleteById(userId);
	}

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

	private void checkUserFieldsFilled(User user) {
		if (user.getGivenName() == null || user.getGivenName().equals(""))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must include a first name.");
		if (user.getFamilyName() == null || user.getFamilyName().equals(""))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must include a last name.");
		if (user.getEmail() == null || user.getEmail().equals(""))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must include an email.");
		if (user.getUsername() == null || user.getUsername().equals(""))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must include a username.");
		if (user.getPassword() == null || user.getPassword().equals(""))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must include a password.");
		if (user.getPhone() == null || user.getPhone().equals(""))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must include a phone number.");
	}

}
