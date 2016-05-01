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
import com.jtrackseries.web.rest.dto.StatsRecordsByCreateDate;

/**
 * Spring Data JPA repository for the Episode entity.
 */
public interface EpisodeRepository extends JpaRepository<Episode, Long> {

	@Query("select episode from Episode episode where episode.datePublish>=?1 and episode.datePublish<=?2 and episode.serie.user.login = ?#{principal.username}")
	List<Episode> findAllByDatePublishBetweenAndUserIsCurrentUser(LocalDate fromDate, LocalDate toDate);
		
	@Query("select episode from Episode episode where episode.serie.user.login = ?#{principal.username}")
	Page<Episode> findByUserIsCurrentUser(Pageable pageable);
	
	List<Episode> findAllByDatePublishBetween(LocalDate fromDate, LocalDate toDate);

	Page<Episode> findAllBySerieId(Long serieId, Pageable pageable);
    
	@Transactional
	@Modifying
	@Query("update Episode e set e.viewed = ?2 where e.id = ?1")
	int setViewed(Long id, Boolean viewed);

	@Query(value = 
			"SELECT new com.jtrackseries.web.rest.dto.StatsRecordsByCreateDate "
			+ "(FUNCTION('YEAR', ep.createdDate) , FUNCTION('MONTH', ep.createdDate) , count(ep) as total ) " 
		    + "FROM Episode ep "
		    + "GROUP BY FUNCTION('YEAR', ep.createdDate) , FUNCTION('MONTH', ep.createdDate) "
		    + "ORDER BY FUNCTION('YEAR', ep.createdDate) , FUNCTION('MONTH', ep.createdDate) "		    
		  )		
	public List<StatsRecordsByCreateDate> findStatsRecordsCreateDate();
}
