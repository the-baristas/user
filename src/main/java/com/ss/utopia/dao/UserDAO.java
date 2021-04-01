package com.ss.utopia.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ss.utopia.entity.User;

@Repository
public interface UserDAO extends JpaRepository<User, Integer>{

	@Query("FROM User WHERE email = ?1")
	List<User> findByUserEmail(String email);
	
	@Query("FROM User WHERE givenName = ?1 AND familyName = ?2")
	List<User> findByName(String givenName, String familyName);
}
