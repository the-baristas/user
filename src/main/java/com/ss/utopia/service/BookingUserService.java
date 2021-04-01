package com.ss.utopia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.ss.utopia.dao.BookingUserDAO;
import com.ss.utopia.entity.BookingUser;

@Service
public class BookingUserService {
	@Autowired
	private BookingUserDAO bookingUserDAO;
	
	public BookingUser getByBookingId(Integer bookingId) {
		return bookingUserDAO.findById(bookingId).get();
	}
	
	public List<BookingUser> getAllBookingUsers(){
		return bookingUserDAO.findAll();
	}
	
	public BookingUser addBookingUser(BookingUser bookingUser) {
		return bookingUserDAO.save(bookingUser);
	}
	
	public void updateBookingUser(BookingUser bookingUser) {
		try {
			bookingUserDAO.findById(bookingUser.getUserId()).get();
		} catch(DataAccessException e){
			e.printStackTrace();
		}
		bookingUserDAO.save(bookingUser);
	}
	
	public void deleteByBookingId(Integer bookingId) {
		try {
			bookingUserDAO.deleteById(bookingId);
		} catch(DataAccessException e){
			e.printStackTrace();
		}
	}
}
