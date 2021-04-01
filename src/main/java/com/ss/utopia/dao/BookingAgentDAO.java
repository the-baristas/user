package com.ss.utopia.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.utopia.entity.BookingAgent;

public interface BookingAgentDAO extends JpaRepository<BookingAgent, Integer> {

}
