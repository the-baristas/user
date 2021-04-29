package com.ss.utopia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.dao.UserRoleDAO;
import com.ss.utopia.entity.UserRole;

@Service
public class UserRoleService {
	
	@Autowired
	UserRoleDAO userRoleDao;
	
	public UserRole getUserRoleByRoleName(String roleName) {
		return userRoleDao.findByroleName(roleName);
	}

}
