package com.educacionit.digitalers.blog.controllers;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educacionit.digitalers.blog.dtos.UserDTO;
import com.educacionit.digitalers.blog.dtos.repositories.UserDTOImpl;
import com.educacionit.digitalers.blog.enums.MessageType;
import com.educacionit.digitalers.blog.exceptions.ExceptionDTO;
import com.educacionit.digitalers.blog.services.ResponseMessageService;

@RestController
@RequestMapping(value = { "/users" }, produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserController implements GenericRestController<UserDTO, Long> {
	private static Logger logger = LogManager.getLogger();

	@Autowired
	private UserDTOImpl userDTOImpl;

	@Autowired
	private ResponseMessageService responseMessageService;

	public ResponseEntity<?> findById(Long id) {
		logger.info("ID : " + id);
		try {
			UserDTO userDTO = userDTOImpl.findById(id).orElse(null);
			return ResponseEntity.ok(userDTO);
		} catch (ExceptionDTO e) {
			logger.error(e);
			return ResponseEntity.status(404).body(responseMessageService.getResponseMessage(MessageType.NO_ELEMENTS,
					"Usuario con ID " + id + " No encontrado"));
		}
	}

	public ResponseEntity<?> insert(@Valid UserDTO userDTO, BindingResult bindingResult) {

		return save(userDTO, bindingResult);
	}

	public ResponseEntity<?> update(@Valid UserDTO userDTO, BindingResult bindingResult) {

		return save(userDTO, bindingResult);
	}

	public ResponseEntity<?> delete(@Valid UserDTO userDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(400)
					.body(responseMessageService.getResponseMessage(MessageType.VALIDATION_ERROR, bindingResult));
		}

		try {
			userDTOImpl.findByEmail(userDTO.getEmail());
		} catch (ExceptionDTO e) {
			logger.error(e);
			return ResponseEntity.status(404).body(
					responseMessageService.getResponseMessage(MessageType.NO_ELEMENTS, userDTO + " No encontrado"));
		}

		userDTOImpl.delete(userDTO);

		return ResponseEntity.ok(
				responseMessageService.getResponseMessage(MessageType.DELETE_ELEMENT, "Usuario " + userDTO.getEmail())
						+ " eliminado correctamente");
	}

	public ResponseEntity<?> findAll() {
		return ResponseEntity.ok(userDTOImpl.findAll());
	}

	private ResponseEntity<?> save(UserDTO userDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(400)
					.body(responseMessageService.getResponseMessage(MessageType.VALIDATION_ERROR, bindingResult));
		}
		logger.info(userDTO);
		userDTOImpl.save(userDTO);

		userDTO.setMessage("Usuario Guardado Exitosamente");
		return ResponseEntity.ok(userDTO);
	}

}
