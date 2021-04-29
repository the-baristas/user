package com.ss.utopia.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ss.utopia.entity.UserRole;

@Repository
public interface UserRoleDAO extends JpaRepository<UserRole, Integer> {
	
	UserRole findByroleName(String roleName);
}
