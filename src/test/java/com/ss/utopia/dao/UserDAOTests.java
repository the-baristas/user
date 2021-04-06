package com.ss.utopia.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.ss.utopia.entity.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

@DataJpaTest
class UserDAOTests {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UserDAO userDao;
	
    @AfterEach
    public void tearDown() {
        entityManager.getEntityManager()
                .createNativeQuery("alter table user alter id restart with 1")
                .executeUpdate();
    }
	
	@Test
	public void testFindById() {

		User user = new User();
		//user.setUserId(1);
		user.setGivenName("First");
		user.setFamilyName("Last");
		user.setUsername("someUsername23");
		user.setEmail("username@email.org");
		user.setIsActive(true);
		user.setPhone("1111111111");
		user.setRole(2);
		user.setPassword("pass");

		entityManager.persist(user);
		entityManager.flush();
		
		User userFromDB = userDao.findById(1).get();
		assertThat(userFromDB.getUserId(), is(user.getUserId()));
		assertThat(userFromDB.getGivenName(), is(user.getGivenName()));
	}

	@Test void testUpdateUser() {
		User user = new User();
		//user.setUserId(1);
		user.setGivenName("First");
		user.setFamilyName("Last");
		user.setUsername("someUsername23");
		user.setEmail("username@email.org");
		user.setIsActive(true);
		user.setPhone("1111111111");
		user.setRole(2);
		user.setPassword("pass");
		
		entityManager.persist(user);
		//entityManager.
		entityManager.flush();
		
		User userFromDB = userDao.findById(1).get();
		assertThat(userFromDB.getUserId(), is(user.getUserId()));
	}
	
}
