package com.educacionit.digitalers.blog.controllers;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educacionit.digitalers.blog.dtos.UserLoginDTO;
import com.educacionit.digitalers.blog.entities.User;
import com.educacionit.digitalers.blog.enums.MessageType;
import com.educacionit.digitalers.blog.repositories.UserRepository;
import com.educacionit.digitalers.blog.services.LoginService;
import com.educacionit.digitalers.blog.services.ResponseMessageService;
import com.octaviorobleto.commons.utilities.text.CodeUtils;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = { "/login" }, produces = { MediaType.APPLICATION_JSON_VALUE })
public class LoginController {
	private static Logger logger = LogManager.getLogger();

	@Value("${database.key}")
	private String KEY_AES;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ResponseMessageService responseMessageService;

	@Autowired
	private LoginService loginService;

	@PostMapping(value = { "/signIn" }, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> signIn(@RequestBody @Valid UserLoginDTO userLoginDTO, BindingResult bindingResult) {

		User user = userRepository.findByEmail(userLoginDTO.getEmail()).orElse(null);
		logger.info(user);
		if (user == null) {
			return ResponseEntity.status(404).body(
					responseMessageService.getResponseMessage(MessageType.NO_ELEMENTS, "Credenciales Incorrectas"));
		} else if (user.getFailedAttemps() > 2) {
			return ResponseEntity.status(404)
					.body(responseMessageService.getResponseMessage(MessageType.USER_BLOCKED, "Usuario Bloqueado"));
		} else if (!CodeUtils.AES_Decrypt(user.getKey(), user.getEmail().concat(KEY_AES))
				.equals(userLoginDTO.getKey())) {
			userRepository.updateFailedAttemps((byte) (user.getFailedAttemps() + 1), user.getEmail());
			return ResponseEntity.status(404).body(
					responseMessageService.getResponseMessage(MessageType.NO_ELEMENTS, "Credenciales Incorrectas"));
		}

		if (user.getFailedAttemps() <= 2) {
			userRepository.updateFailedAttemps((byte) 0, user.getEmail());
		}

		return ResponseEntity.ok(loginService.getLogin(userLoginDTO.getEmail()));
	}

	@GetMapping(value = { "/signOff/{email}" })
	public ResponseEntity<?> signOff(@PathVariable(name = "email", required = true) String email) {
		return null;
	}

}
