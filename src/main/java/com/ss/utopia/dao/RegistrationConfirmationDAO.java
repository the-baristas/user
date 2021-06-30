package com.ss.utopia.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ss.utopia.entity.RegistrationConfirmation;

@Repository
public interface RegistrationConfirmationDAO extends JpaRepository<RegistrationConfirmation, Long> {

	@Query("FROM RegistrationConfirmation WHERE token = ?1")
	Optional<RegistrationConfirmation> findByToken(String token);
}
