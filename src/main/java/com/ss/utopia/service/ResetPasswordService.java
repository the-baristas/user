package com.ss.utopia.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ss.utopia.Utils;
import com.ss.utopia.dao.ResetPasswordConfirmationDAO;
import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.email.EmailSender;
import com.ss.utopia.entity.RegistrationConfirmation;
import com.ss.utopia.entity.ResetPasswordConfirmation;
import com.ss.utopia.entity.User;
import com.ss.utopia.exception.ConfirmationExpiredException;

@Service
public class ResetPasswordService {
	
	@Autowired
	private ResetPasswordConfirmationDAO confirmationDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private EmailSender emailSender;
	
	private Integer confirmationExpirationMinutes = 15;
	
	public ResetPasswordConfirmation findByToken(String token) {
		return confirmationDAO.findByToken(token).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not a valid confirmation token"));
	}
	
	public ResetPasswordConfirmation handleResetPasswordRequest(User user) {
		
		ResetPasswordConfirmation confirmation = new ResetPasswordConfirmation(
		UUID.randomUUID().toString(),
		LocalDateTime.now(),
		LocalDateTime.now().plusMinutes(confirmationExpirationMinutes),
		user
		);	
		
		ResetPasswordConfirmation saved = confirmationDAO.save(confirmation);
		
		emailSender.sendForgetPasswordEmail(user, saved);
		
		return saved;
	}
	
	@Transactional
	public User changePassword(String token, String newPassword) throws ConfirmationExpiredException {
		ResetPasswordConfirmation confirmation = findByToken(token);
		User user = confirmation.getUser();
		
		LocalDateTime currentTime = LocalDateTime.now();
		
		//Resend email if previous email has expired
		if(currentTime.isAfter(confirmation.getExpiresAt())) {
			
			ResetPasswordConfirmation confirmationRetry = new ResetPasswordConfirmation(
					UUID.randomUUID().toString(),
					LocalDateTime.now(),
					LocalDateTime.now().plusMinutes(confirmationExpirationMinutes),
					user
					);	
					
			ResetPasswordConfirmation saved = confirmationDAO.save(confirmationRetry);
			emailSender.sendForgetPasswordEmail(user, saved);
			throw new ConfirmationExpiredException(user.getEmail());
		}
		
		confirmation.setConfirmedAt(currentTime);
		confirmationDAO.save(confirmation);
		
		user.setPassword(Utils.passwordEncoder().encode(newPassword));
		userDAO.save(user);
		return user;
	}
}
