package com.ss.utopia;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtilsCheckPasswordEncoderTests {

	String pw1 = "testpassword";
	String pw2 = "d#0r39fmFLKFM3Vm9@#4";
	
	@Test
	void testPasswordEncoderHP() {
		String encoded1Pw1 = Utils.passwordEncoder().encode(pw1);
		String encoded2Pw1 = Utils.passwordEncoder().encode(pw1);
		
		String encoded1Pw2 = Utils.passwordEncoder().encode(pw2);
		String encoded2Pw2 = Utils.passwordEncoder().encode(pw2);
		
		Assertions.assertTrue(Utils.passwordEncoder().matches(pw1, encoded1Pw1));
		Assertions.assertTrue(Utils.passwordEncoder().matches(pw1, encoded2Pw1));
		Assertions.assertTrue(Utils.passwordEncoder().matches(pw2, encoded1Pw2));
		Assertions.assertTrue(Utils.passwordEncoder().matches(pw2, encoded2Pw2));
	}
	
	void testPasswordEncoderFP() {
		String encoded1Pw1 = Utils.passwordEncoder().encode(pw1);
		String encoded2Pw1 = Utils.passwordEncoder().encode(pw1);
		
		String encoded1Pw2 = Utils.passwordEncoder().encode(pw2);
		String encoded2Pw2 = Utils.passwordEncoder().encode(pw2);
		
		Assertions.assertFalse(false);
		
	}

}
