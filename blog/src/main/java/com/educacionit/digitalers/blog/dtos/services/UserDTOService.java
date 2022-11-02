package com.educacionit.digitalers.blog.dtos.services;

import org.springframework.stereotype.Service;

import com.educacionit.digitalers.blog.dtos.UserDTO;
import com.educacionit.digitalers.blog.entities.User;
import com.educacionit.digitalers.blog.exceptions.ExceptionDTO;

@Service
public class UserDTOService {


	public User getUser(UserDTO userDTO) {
		if (userDTO == null) {
			throw new ExceptionDTO("UserDTO Nulo");
		}
		return User.builder().email(userDTO.getEmail()).key(userDTO.getKey()).active(userDTO.getActive()).build();
	}

	public UserDTO getUserDTO(User user, String message) {
		if (user == null) {
			throw new ExceptionDTO("User Nulo");
		}
		UserDTO userDTO = UserDTO.builder().email(user.getEmail()).key(user.getKey()).active(user.getActive())
				.message(message == null ? "" : message).build();
		return userDTO;
	}
}
