package com.ss.utopiauser.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.ss.utopiauser.dao.BookingUserDAO;
import com.ss.utopiauser.entity.BookingUser;

@Service
public class BookingUserService {
	@Autowired
	private BookingUserDAO bookingUserDAO;
	
	public BookingUser getUserById(Integer bookingId) {
		return bookingUserDAO.findById(bookingId).get();
	}
	
	public List<BookingUser> getAllBookingUsers(){
		return bookingUserDAO.findAll();
	}
	
	public BookingUser addUser(BookingUser bookingUser) {
		return bookingUserDAO.save(bookingUser);
	}
	
	public void updateUser(BookingUser bookingUser) {
		try {
			bookingUserDAO.findById(bookingUser.getUserId()).get();
		} catch(DataAccessException e){
			e.printStackTrace();
		}
		bookingUserDAO.save(bookingUser);
	}
	
	public void deleteUserById(Integer bookingId) {
		try {
			bookingUserDAO.deleteById(bookingId);
		} catch(DataAccessException e){
			e.printStackTrace();
		}
	}
}
