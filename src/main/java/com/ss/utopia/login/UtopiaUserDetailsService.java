package com.ss.utopia.login;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.entity.User;

@Service
public class UtopiaUserDetailsService implements UserDetailsService {

	@Autowired
	UserDAO userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<User> users = userDao.findByUsername(username);
		
		if(users.size() == 0)
			throw new UsernameNotFoundException("Username does not exist: " + username);

		return new UtopiaUserDetails(users.get(0));
	}

}
