package com.educacionit.digitalers.blog.controllers;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educacionit.digitalers.blog.dtos.UserDTO;
import com.educacionit.digitalers.blog.dtos.repositories.UserDTOImpl;
import com.educacionit.digitalers.blog.entities.User;
import com.educacionit.digitalers.blog.enums.MessageType;
import com.educacionit.digitalers.blog.exceptions.ExceptionDTO;
import com.educacionit.digitalers.blog.repositories.UserRepository;
import com.educacionit.digitalers.blog.services.LoginService;
import com.educacionit.digitalers.blog.services.ResponseMessageService;

@RestController
@RequestMapping(value = { "/users" }, produces = { MediaType.APPLICATION_JSON_VALUE })
public class UserController implements GenericRestController<UserDTO, Long> {
	private static Logger logger = LogManager.getLogger();

	@Autowired
	private UserDTOImpl userDTOImpl;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ResponseMessageService responseMessageService;

	@Autowired
	private LoginService loginService;

	//Obtener usuario por ID
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

	//Insertar nuevo usuario (UUID requerido)
	public ResponseEntity<?> insert(String uuid, @Valid UserDTO userDTO, BindingResult bindingResult) {
		logger.info("credential :" + uuid);

		if (uuid == null) {
			return ResponseEntity.status(400).body(responseMessageService.getResponseMessage(MessageType.BAD_REQUEST,
					"credential [" + uuid + "] No encontrada"));
		}
		if (loginService.validateLogin(uuid) == null) {
			return ResponseEntity.status(409).body(responseMessageService
					.getResponseMessage(MessageType.VALIDATION_ERROR, "credential [" + uuid + "] No encontrada"));
		}

		return save(userDTO, bindingResult);
	}
	
	//Insertar nuevo usuario (UUID no requerido)
	@PostMapping(value = {"/insertNewUser"}, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> insertNewUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
		logger.info("User: " + userDTO.getEmail());
		
		User user = userRepository.findByEmail(userDTO.getEmail()).orElse(null);
		
		if(user == null)
			return save(userDTO, bindingResult);
		else {
			return ResponseEntity.status(409).body(responseMessageService
					.getResponseMessage(MessageType.EXISTING_USER, "El usuario ya existe"));
		}
	}

	//Actualizar usuario
	public ResponseEntity<?> update(String uuid, @Valid UserDTO userDTO, BindingResult bindingResult) {
		logger.info("credential :" + uuid);

		if (uuid == null) {
			return ResponseEntity.status(400).body(responseMessageService.getResponseMessage(MessageType.BAD_REQUEST,
					"credential [" + uuid + "] No encontrada"));
		}

		if (loginService.validateLogin(uuid) == null) {
			return ResponseEntity.status(409).body(responseMessageService
					.getResponseMessage(MessageType.VALIDATION_ERROR, "credential [" + uuid + "] No encontrada"));
		}
		return save(userDTO, bindingResult);
	}

	//Eliminar usuario
	public ResponseEntity<?> delete(String uuid, @Valid UserDTO userDTO, BindingResult bindingResult) {
		logger.info("credential :" + uuid);

		if (uuid == null) {
			return ResponseEntity.status(400).body(responseMessageService.getResponseMessage(MessageType.BAD_REQUEST,
					"credential [" + uuid + "] No encontrada"));
		}
		if (loginService.validateLogin(uuid) == null) {
			return ResponseEntity.status(409).body(responseMessageService
					.getResponseMessage(MessageType.VALIDATION_ERROR, "credential [" + uuid + "] No encontrada"));
		}
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

	//Obtener todos los usuarios
	public ResponseEntity<?> findAll() {
		return ResponseEntity.ok(userDTOImpl.findAll());
	}

	//-----------------------------------------------------------------------------
	
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