package com.jtrackseries.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtrackseries.domain.Serie;
import com.jtrackseries.web.rest.dto.StatsSerieSeasonViewedDTO;

/**
 * Spring Data JPA repository for the Serie entity.
 */
public interface SerieRepository extends JpaRepository<Serie, Long> {

	List<Serie> findAllByTitle(String title);

    @Query("select serie from Serie serie where serie.externalId=?1 and serie.user.login = ?#{principal.username}")
    Optional<Serie> findOneByExternalIdAndUserIsCurrentUser(String externalId);

    @Query("select serie from Serie serie where serie.user.login = ?#{principal.username}")
	Page<Serie> findByUserIsCurrentUser(Pageable pageable);

    @Query("select serie from Serie serie where serie.user.login = ?#{principal.username}")
	List<Serie> findByUserIsCurrentUser();
    
	@Query(value = 
			"SELECT new com.jtrackseries.web.rest.dto.StatsSerieSeasonViewedDTO "
			+ "(ep.serie,"
			+ " ep.season,"
			+ " SUM(CASE WHEN ep.viewed = true THEN 1 ELSE 0 END) AS totalViewed, "
			+ " count(ep) as totalEpisodes ) " 
		    + "FROM Episode ep "
		    + "WHERE ep.serie.id = ?2 AND ep.season=?1 "
		    + "GROUP BY ep.serie, ep.season"
		  )
	StatsSerieSeasonViewedDTO findOneStatsBySeasonAndSerieId(String season, Long serieId);
		
	@Query("SELECT max(ep.season) FROM Episode ep WHERE ep.serie.id = ?1 ")
	Integer getMaxSeasonBySerieId(Long serieId);

	@Query("SELECT case when (count(ep) > 0)  then true else false end  FROM Episode ep WHERE ep.serie.id = ?1 and ep.season > ?2 ")
	Boolean hasMoreSeason (Long serieId, String season);
	
	@Query(value = 
			"SELECT new com.jtrackseries.web.rest.dto.StatsSerieSeasonViewedDTO "
			+ "(ep.serie,"
			+ " ep.season,"
			+ " SUM(CASE WHEN ep.viewed = true THEN 1 ELSE 0 END) AS totalViewed, "
			+ " count(ep) as totalEpisodes ) " 
		    + "FROM Episode ep "
		    + "WHERE "
		    + " ep.serie.user.login = ?#{principal.username} "
			+ " AND ep.season in ( " 
		    + "   SELECT epInternal.season " 
	        + "     FROM Episode epInternal "
			+ "    WHERE epInternal.serie=ep.serie "
			+ "	     AND epInternal.season=ep.season "
			+ "	     AND epInternal.datePublish > CURRENT_DATE"
	        + "   ) "  			
		    + "GROUP BY ep.serie, ep.season "
		    + "ORDER BY ep.serie, ep.season "
		  )	
	public List<StatsSerieSeasonViewedDTO> findLastSeriesStats();
			
	
}
