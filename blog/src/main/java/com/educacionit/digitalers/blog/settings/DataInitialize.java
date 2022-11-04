package com.educacionit.digitalers.blog.settings;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.educacionit.digitalers.blog.entities.User;
import com.educacionit.digitalers.blog.repositories.UserRepository;
import com.octaviorobleto.commons.utilities.text.CodeUtils;

@Component
public class DataInitialize implements CommandLineRunner {
	private static Logger logger = LogManager.getLogger();
	@Value("${database.key}")
	private String KEY_AES;
	@Autowired
	private UserRepository userRepository;

	public void run(String... args) throws Exception {
		users();
	}

	private void users() {
		logger.info("users...");
		logger.info(KEY_AES);
		userRepository.save(User.builder().email("user1@gmail.com")
				.key(CodeUtils.AES_Encrypt("1234", "user1@gmail.com".concat(KEY_AES))).active(true)
				.creationDate(LocalDateTime.now()).build());
		userRepository.save(User.builder().email("user2@gmail.com")
				.key(CodeUtils.AES_Encrypt("1234", "user2@gmail.com".concat(KEY_AES))).active(false)
				.creationDate(LocalDateTime.now()).build());

		logger.info(userRepository.findById(1L));
		logger.info(userRepository.findByEmail("user2@gmail.com"));
		
	}

}