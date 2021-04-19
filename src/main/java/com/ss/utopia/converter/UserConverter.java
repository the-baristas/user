package com.ss.utopia.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ss.utopia.dto.UserDTO;
import com.ss.utopia.entity.User;

public class UserConverter {

	
	static public UserDTO entityToDto(User user) {
		ModelMapper mapper = new ModelMapper();
		
		return mapper.map(user, UserDTO.class);
	}
	
	static public User dtoToEntity(UserDTO userDto) {
		ModelMapper mapper = new ModelMapper();
		
		return mapper.map(userDto, User.class);
	}
}
