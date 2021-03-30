package com.ss.utopiauser.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.ss.utopiauser.dao.UserDAO;
import com.ss.utopiauser.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	public User getUserById(Integer userId) {
		return userDAO.findById(userId).get();
	}
	
	public List<User> getAllUsers(){
		return userDAO.findAll();
	}
	
	public User addUser(User user) {
		return userDAO.save(user);
	}
	
	public void updateUser(User user) {
		try {
			userDAO.findById(user.getUserId()).get();
		} catch(DataAccessException e){
			e.printStackTrace();
		}
		userDAO.save(user);
	}
	
	public void deleteUserById(Integer userId) {
		try {
			userDAO.deleteById(userId);
		} catch(DataAccessException e){
			e.printStackTrace();
		}
	}
}
