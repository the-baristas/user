package com.ss.utopia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ss.utopia.dao.RegistrationConfirmationDAO;
import com.ss.utopia.entity.RegistrationConfirmation;

@Service
public class RegistrationConfirmationService {
	
	@Autowired
	RegistrationConfirmationDAO confirmationDAO;
	
	public RegistrationConfirmation findByToken(String token) {
		return confirmationDAO.findByToken(token).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not a valid confirmation token"));
	}
}
