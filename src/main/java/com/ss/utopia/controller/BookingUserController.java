package com.ss.utopia.controller;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ss.utopia.entity.BookingUser;
import com.ss.utopia.service.BookingUserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/utopia_airlines")
public class BookingUserController {
	@Autowired
	private BookingUserService bookingUserService;
	
	@GetMapping("/bookingUser")
	public List<BookingUser> getAllUser(){
		return bookingUserService.getAllBookingUsers();
	}
	@GetMapping("/bookingUser/{bookingId}")
	public BookingUser getUserById(@PathVariable("bookingId") Integer bookingId) {
		return bookingUserService.getByBookingId(bookingId);
	}
	
	@PostMapping("/bookingUser")
	@ResponseStatus(HttpStatus.CREATED)
	public BookingUser createUser(@Valid @RequestBody BookingUser bookingUser) {
		return bookingUserService.addBookingUser(bookingUser);
	}
	
	@PutMapping("/bookingUser")
	public ResponseEntity<String> updateBookingUser(@RequestBody BookingUser bookingUser){
		try {
			bookingUserService.updateBookingUser(bookingUser);
			return new ResponseEntity<String>(HttpStatus.OK);
		}catch(NoSuchElementException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/user/{bookingId}")
	public ResponseEntity<String> deleteBookingUser(@PathVariable Integer bookingId){
		try{
			bookingUserService.deleteByBookingId(bookingId);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch(RuntimeException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
	}
}
