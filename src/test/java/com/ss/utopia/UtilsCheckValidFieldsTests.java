package com.ss.utopia;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

class UtilsCheckValidFieldsTests {
	
	@Test
	void testEmailValidThrowsException() {
		assertThrows( ResponseStatusException.class, () -> {Utils.checkEmailValid("notavalidemail.com");} );
		assertThrows( ResponseStatusException.class, () -> {Utils.checkEmailValid("invalid@.org");} );
		assertThrows( ResponseStatusException.class, () -> {Utils.checkEmailValid("@gmail.com");} );
		assertThrows( ResponseStatusException.class, () -> {Utils.checkEmailValid("@.com");} );
		assertThrows( ResponseStatusException.class, () -> {Utils.checkEmailValid("email@gmail.");} );
		assertThrows( ResponseStatusException.class, () -> {Utils.checkEmailValid("email@gmail");} );
		assertThrows( ResponseStatusException.class, () -> {Utils.checkEmailValid("em@ail@gmail.com");} );
		assertThrows( ResponseStatusException.class, () -> {Utils.checkEmailValid("email.gmail.com");} );
	}

	@Test
	void testEmailValidDoesNotThrowException() {
		assertDoesNotThrow(() -> {Utils.checkEmailValid("email@gmail.com");});
		assertDoesNotThrow(() -> {Utils.checkEmailValid("anthony2345@yahoo.com");});
		assertDoesNotThrow(() -> {Utils.checkEmailValid("df5@website.gov");});
		assertDoesNotThrow(() -> {Utils.checkEmailValid("valid.email@site.ca");});
	}
	
	@Test
	void testPhoneNumberValidThrowsException() {
		assertThrows( ResponseStatusException.class, () -> {Utils.checkPhoneNumberValid("7");} );
		assertThrows( ResponseStatusException.class, () -> {Utils.checkPhoneNumberValid("99945677");} );
		assertThrows( ResponseStatusException.class, () -> {Utils.checkPhoneNumberValid("888 888 8888");} );
		assertThrows( ResponseStatusException.class, () -> {Utils.checkPhoneNumberValid("777-555-4848");} );
		
	}
	
	@Test
	void testPhoneNumberValidDoesNotThrowException() {
		assertDoesNotThrow(() -> {Utils.checkPhoneNumberValid("2825556756");});
	}
	
}
