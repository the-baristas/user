package com.ss.utopia.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ss.utopia.entity.ResetPasswordConfirmation;


public interface ResetPasswordConfirmationDAO extends JpaRepository<ResetPasswordConfirmation, Long> {

	@Query("FROM ResetPasswordConfirmation WHERE token = ?1")
	Optional<ResetPasswordConfirmation> findByToken(String token);
}