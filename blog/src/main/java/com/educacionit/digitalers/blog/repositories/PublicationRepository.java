package com.educacionit.digitalers.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.educacionit.digitalers.blog.entities.Publication;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
	List<Publication> findByUserId(Long id);
}
