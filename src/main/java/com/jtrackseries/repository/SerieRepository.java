package com.jtrackseries.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtrackseries.domain.Serie;

/**
 * Spring Data JPA repository for the Serie entity.
 */
public interface SerieRepository extends JpaRepository<Serie, Long> {

	List<Serie> findAllByTitle(String title);

}
