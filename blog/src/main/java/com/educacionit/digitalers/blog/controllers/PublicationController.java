package com.educacionit.digitalers.blog.controllers;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educacionit.digitalers.blog.entities.Publication;
import com.educacionit.digitalers.blog.entities.User;
import com.educacionit.digitalers.blog.enums.MessageType;
import com.educacionit.digitalers.blog.exceptions.ExceptionDTO;
import com.educacionit.digitalers.blog.repositories.PublicationRepository;
import com.educacionit.digitalers.blog.services.LoginService;
import com.educacionit.digitalers.blog.services.ResponseMessageService;

@RestController
@RequestMapping(value = { "/publications" }, produces = { MediaType.APPLICATION_JSON_VALUE })
public class PublicationController implements GenericRestController<Publication, Long> {
	
	private static Logger logger = LogManager.getLogger();
	
	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired
	private ResponseMessageService responseMessageService;
	
	@Autowired
	private LoginService loginService;

	public ResponseEntity<?> findById(Long id) {

		logger.info("ID : " + id);
		Publication publication = publicationRepository.findById(id).orElse(null);
		if (publication == null) {
			return ResponseEntity.status(404).body(responseMessageService.getResponseMessage(MessageType.NO_ELEMENTS,
					"Publicacion con ID " + id + " No encontrada"));
		}

		return ResponseEntity.ok(publication);
	}

	public ResponseEntity<?> insert(String uuid, @Valid Publication publication, BindingResult bindingResult) {
		logger.info("credential :" + uuid);

		if (uuid == null) {
			return ResponseEntity.status(400).body(responseMessageService.getResponseMessage(MessageType.BAD_REQUEST,
					"credential [" + uuid + "] No encontrada"));
		}
		User user = loginService.validateLogin(uuid);
		if (user == null) {
			return ResponseEntity.status(409).body(responseMessageService
					.getResponseMessage(MessageType.VALIDATION_ERROR, "credential [" + uuid + "] No encontrada"));
		}

		publication.setUser(user);
		return save(publication,bindingResult);
	}


	public ResponseEntity<?> findAll() {
		List<Publication> publications = publicationRepository.findAll();
		logger.info(publications);
		return ResponseEntity.ok(publications);
	}

	@GetMapping(value = { "/findByUserId/{user_id}" })
	public ResponseEntity<?> findAll(@PathVariable(name = "user_id") Long userId) {
		
		logger.info("UserID :" + userId);

		List<Publication> publications = publicationRepository.findByUserId(userId);
		logger.info(publications);
		return ResponseEntity.ok(publications);
	}
	
	
	public ResponseEntity<?> delete(String uuid, @Valid Publication publication, BindingResult bindingResult) {
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
			publicationRepository.findById(publication.getId()).orElse(null);
		} catch (Exception e) {
			logger.error(e);
			return ResponseEntity.status(404).body(
					responseMessageService.getResponseMessage(MessageType.NO_ELEMENTS, " No encontrado"));
		}

		publicationRepository.delete(publication);

		return ResponseEntity.ok(
				responseMessageService.getResponseMessage(MessageType.DELETE_ELEMENT, "Publicacion eliminada correctamente"));
	}
	
	//NO IMPLEMENTADO-----------------------------------------------
	
		public ResponseEntity<?> update(String uuid, @Valid Publication publication, BindingResult bindingResult) {

			return null;
		}
		
	//--------------------------------------------------------
	
	private ResponseEntity<?> save(Publication publication, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(400)
					.body(responseMessageService.getResponseMessage(MessageType.VALIDATION_ERROR, bindingResult));
		}
		logger.info(publication);
		publicationRepository.save(publication);

		return ResponseEntity.ok(publication);
	}

}
