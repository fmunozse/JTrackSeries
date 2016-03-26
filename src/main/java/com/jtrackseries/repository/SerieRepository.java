package com.jtrackseries.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtrackseries.domain.Serie;

/**
 * Spring Data JPA repository for the Serie entity.
 */
public interface SerieRepository extends JpaRepository<Serie, Long> {

	List<Serie> findAllByTitle(String title);

    Optional<Serie> findOneByExternalId(String externalId);
	
}
