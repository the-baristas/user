package com.ss.utopia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.dto.ResetPasswordRequestDTO;
import com.ss.utopia.entity.ResetPasswordConfirmation;
import com.ss.utopia.exception.ConfirmationExpiredException;
import com.ss.utopia.service.ResetPasswordService;
import com.ss.utopia.service.UserService;

@RestController
@RequestMapping("/users/password")
public class ResetPasswordController {
	
	@Autowired
	private ResetPasswordService resetPasswordService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("{email}")
	public ResponseEntity<String> createResetPasswordRequest(@PathVariable String email) {
		
		ResetPasswordConfirmation confirmation = resetPasswordService.handleResetPasswordRequest(userService.getUserByEmail(email));
		return ResponseEntity.status(HttpStatus.CREATED).body(confirmation.getToken());
	}
	
	@PutMapping
	public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO request) throws ConfirmationExpiredException{
		resetPasswordService.changePassword(request.getToken(), request.getPassword());
		
		return ResponseEntity.status(HttpStatus.CREATED).body("Password changed successfully.");
		
	}
	
	

}
