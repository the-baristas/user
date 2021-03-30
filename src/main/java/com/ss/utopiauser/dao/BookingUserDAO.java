package com.ss.utopiauser.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.utopiauser.entity.BookingUser;

public interface BookingUserDAO extends JpaRepository<BookingUser, Integer> {

}
