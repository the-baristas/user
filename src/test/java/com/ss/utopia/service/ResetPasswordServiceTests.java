package com.ss.utopia.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.ss.utopia.dao.ResetPasswordConfirmationDAO;
import com.ss.utopia.dao.UserDAO;
import com.ss.utopia.email.EmailSender;
import com.ss.utopia.entity.ResetPasswordConfirmation;
import com.ss.utopia.entity.User;
import com.ss.utopia.entity.UserRole;
import com.ss.utopia.exception.ConfirmationExpiredException;

@ExtendWith(MockitoExtension.class)
public class ResetPasswordServiceTests {

	@InjectMocks
	private ResetPasswordService service;

	@Mock
	private ResetPasswordConfirmationDAO confirmationDao;
	
	@Mock
	private UserDAO userDao;
	
	@Mock
	private EmailSender emailSender;
	
	@Test
	void testFindByToken() {
		User user = makeUser();
		ResetPasswordConfirmation confirmation = makeConfirmation(user);
		
		when(confirmationDao.save(any())).thenReturn(confirmation);

		assertTrue(service.handleResetPasswordRequest(user).equals(confirmation));
	}
	
	@Test
	void handleResetPasswordRequest() {
		
	}
	
	@Test
	void testChangePassword() throws ConfirmationExpiredException {
		User user = makeUser();
		ResetPasswordConfirmation confirmation = makeConfirmation(user);
		String newPassword = "new";
		
		when(confirmationDao.findByToken(confirmation.getToken())).thenReturn(Optional.of(confirmation));
		when(confirmationDao.save(confirmation)).thenReturn(confirmation);
		when(userDao.save(user)).thenReturn(user);
		
		assertTrue(service.changePassword(confirmation.getToken(), newPassword).equals(user));
	}
	
	@Test
	void testChangePasswordExpiredThrowsException() throws ConfirmationExpiredException {
		User user = makeUser();
		ResetPasswordConfirmation confirmation = makeConfirmation(user);
		confirmation.setExpiresAt(LocalDateTime.now().minusMinutes(10));
		String newPassword = "new";
		
		when(confirmationDao.findByToken(confirmation.getToken())).thenReturn(Optional.of(confirmation));
		when(confirmationDao.save(any())).thenReturn(confirmation);
		Assertions.assertThrows(ResponseStatusException.class, () -> {
			service.changePassword(confirmation.getToken(), newPassword);});
	}
	
	
	private ResetPasswordConfirmation makeConfirmation(User user) {
		return new ResetPasswordConfirmation(
				"token",
				LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(3),
				user
				);	
	}
	
	private User makeUser() {
		User user = new User();
		user.setUserId(1);
		user.setGivenName("First");
		user.setFamilyName("Last");
		user.setUsername("someusername23");
		user.setEmail("username@email.org");
		user.setActive(true);
		user.setPhone("1111111111");
		user.setRole(new UserRole(2, "ROLE_ADMIN"));
		user.setPassword("pass");
		return user;
	}
}
