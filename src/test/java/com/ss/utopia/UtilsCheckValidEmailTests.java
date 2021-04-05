package com.ss.utopia;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


import com.ss.utopia.exception.EmailException;

class UtilsCheckValidEmailTests {
	
	@Test
	void testEmailValidThrowsException() {
		assertThrows( EmailException.class, () -> {Utils.checkEmailValid("notavalidemail.com");} );
		assertThrows( EmailException.class, () -> {Utils.checkEmailValid("invalid@.org");} );
		assertThrows( EmailException.class, () -> {Utils.checkEmailValid("@gmail.com");} );
		assertThrows( EmailException.class, () -> {Utils.checkEmailValid("@.com");} );
		assertThrows( EmailException.class, () -> {Utils.checkEmailValid("email@gmail.");} );
		assertThrows( EmailException.class, () -> {Utils.checkEmailValid("email@gmail");} );
		assertThrows( EmailException.class, () -> {Utils.checkEmailValid("em@ail@gmail.com");} );
		assertThrows( EmailException.class, () -> {Utils.checkEmailValid("email.email@gmail.com");} );
		assertThrows( EmailException.class, () -> {Utils.checkEmailValid("email.gmail.com");} );
	}

	@Test
	void testEmailValidDoesNotThrowException() {
		assertDoesNotThrow(() -> {Utils.checkEmailValid("email@gmail.com");});
		assertDoesNotThrow(() -> {Utils.checkEmailValid("anthony2345@yahoo.com");});
		assertDoesNotThrow(() -> {Utils.checkEmailValid("df5@website.gov");});
		assertDoesNotThrow(() -> {Utils.checkEmailValid("validemail@site.ca");});
	}
	
}
