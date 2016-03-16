package com.jtrackseries.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jtrackseries.domain.Episode;

/**
 * Spring Data JPA repository for the Episode entity.
 */
public interface EpisodeRepository extends JpaRepository<Episode, Long> {

	List<Episode> findAllByDatePublishBetween(LocalDate fromDate, LocalDate toDate);

	Page<Episode> findAllBySerieId(Long serieId, Pageable pageable);

}
