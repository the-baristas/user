package com.ss.utopia.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.utopia.entity.BookingUser;

public interface BookingUserDAO extends JpaRepository<BookingUser, Integer> {

}
