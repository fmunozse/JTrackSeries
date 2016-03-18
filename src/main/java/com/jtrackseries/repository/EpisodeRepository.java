package com.jtrackseries.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jtrackseries.domain.Episode;

/**
 * Spring Data JPA repository for the Episode entity.
 */
public interface EpisodeRepository extends JpaRepository<Episode, Long> {

	List<Episode> findAllByDatePublishBetween(LocalDate fromDate, LocalDate toDate);

	Page<Episode> findAllBySerieId(Long serieId, Pageable pageable);

	@Transactional
	@Modifying
	@Query("update Episode e set e.viewed = ?2 where e.id = ?1")
	int setViewed(Long id, Boolean viewed);

}