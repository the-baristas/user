package com.ss.utopia.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ss.utopia.dao.UserRoleDAO;
import com.ss.utopia.entity.UserRole;

@ExtendWith(MockitoExtension.class)

class UserRoleServiceTests {

	@InjectMocks
	private UserRoleService userRoleService;
	
	@Mock
	private UserRoleDAO userRoleDao;
	
	@Test
	void testGetByRoleName() {
		UserRole role = new UserRole(2, "ROLE_ADMIN");
		
		when(userRoleDao.findByroleName(role.getRoleName())).thenReturn(role);
		
		UserRole roleFromDB = userRoleService.getUserRoleByRoleName(role.getRoleName());
		
		assertThat(roleFromDB, is(role));
	}
	
}
