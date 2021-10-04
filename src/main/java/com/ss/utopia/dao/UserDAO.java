package com.ss.utopia.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ss.utopia.entity.User;

@Repository
public interface UserDAO extends JpaRepository<User, Integer>{

	@Query("FROM User WHERE email = ?1")
	List<User> findByUserEmail(String email);
	
	@Query("FROM User WHERE username = ?1")
	List<User> findByUsername(String username);
	
	@Query("FROM User WHERE phone = ?1")
	List<User> findByPhoneNumber(String phone);
	
	@Query("FROM User WHERE givenName = ?1 AND familyName = ?2")
	List<User> findByName(String givenName, String familyName);
	
	@Query("SELECT u FROM User u WHERE u.isActive = ?1")
	Page<User> findAllActive(Boolean active, Pageable pageable);
	
    @Query("SELECT u FROM User u WHERE u.username LIKE %:searchTerm% OR u.email LIKE %:searchTerm% OR u.givenName LIKE %:searchTerm% OR u.familyName LIKE %:searchTerm% OR u.phone LIKE %:searchTerm%")
    Page<User> findDistinctBySearchTerm(
            @Param("searchTerm") String searchTerm, Pageable pageable);
        
    @Query("SELECT u FROM User u WHERE u.isActive = ?2 AND (u.username LIKE %?1% OR u.email LIKE %?1% OR u.givenName LIKE %?1% OR u.familyName LIKE %?1% OR u.phone LIKE %?1%)")
    Page<User> findDistinctBySearchTerm(
            String searchTerm, Boolean active, Pageable pageable);

}
