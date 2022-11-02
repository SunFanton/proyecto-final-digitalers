package com.educacionit.digitalers.blog.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PrincipalController {
	private static Logger logger = LogManager.getLogger();

	@GetMapping(value = { "/welcome", "/index", "/blog" })
	public String welcome() {
		logger.info("welcome page");
		return "welcome.html";
	}

}
