package com.jtrackseries.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codahale.metrics.annotation.Timed;
import com.jtrackseries.domain.Serie;
import com.jtrackseries.repository.EpisodeRepository;
import com.jtrackseries.repository.SerieRepository;
import com.jtrackseries.repository.UserRepository;
import com.jtrackseries.security.SecurityUtils;
import com.jtrackseries.web.rest.dto.ScratchSeriesDTO;
import com.jtrackseries.web.rest.dto.StatsSincronyzeDTO;
import com.omertron.thetvdbapi.TheTVDBApi;
import com.omertron.thetvdbapi.TvDbException;
import com.omertron.thetvdbapi.model.Episode;
import com.omertron.thetvdbapi.model.Series;

@Service
@Transactional
public class TVDBScratchService {

	private final Logger log = LoggerFactory.getLogger(TVDBScratchService.class);

	@Inject
	SerieRepository serieRepository;

	@Inject
	EpisodeRepository episodeRepository;
	
	@Inject
	UserRepository userRepository;

    @Value("${jtrackseries.tvdb.token}")
    private String tvdbToken;
	
  
    
	@Timed
	public Serie importSeriesById(String id, String language) {
		TheTVDBApi tvDB = new TheTVDBApi(tvdbToken);


		Serie serie = new Serie();

		try {
			Series series = tvDB.getSeries(id, language);
			log.info("Series : {} ", series);

			serie.setDescription(StringUtils.left(parseStringSafe(series.getOverview()),2048));
			serie.setExternalId(parseStringSafe(series.getId()));
			// serie.setExternalLink(externalLink);
			serie.setFirstAired(parseLocalDateSafe(series.getFirstAired()));
			serie.setImdbId(parseStringSafe(series.getImdbId()));
			serie.setStatus(parseStringSafe(series.getStatus()));
			serie.setTitle(StringUtils.left(parseStringSafe(series.getSeriesName()),2048));			
			serie.setLastUpdated(convertUnixDateTimeToLastUpdated (series.getLastUpdated()));
						
			serie.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get() );
			
			log.debug("serie to save {}", serie);
			serieRepository.save(serie);

			List<Episode> lEpisodes = tvDB.getAllEpisodes(id, language);
			for (Episode eTvDb : lEpisodes) {
				log.info("Episode : {} ", eTvDb);

				com.jtrackseries.domain.Episode episode = new com.jtrackseries.domain.Episode();
				LocalDate firstAired = parseLocalDateSafe(eTvDb.getFirstAired());
				episode.setDatePublish(firstAired);
				episode.setDescription(StringUtils.left(parseStringSafe(eTvDb.getOverview()),2048));
				episode.setEpisodeNumber(eTvDb.getEpisodeNumber());
				episode.setExternalId(parseStringSafe(eTvDb.getId()));
				episode.setSeason(String.valueOf(eTvDb.getSeasonNumber()));
				episode.setSerie(serie);
				episode.setTitle(StringUtils.left(StringUtils.defaultString(parseStringSafe(eTvDb.getEpisodeName()), "NOT DEFINED"),2048));				
				episode.setLastUpdated(convertUnixDateTimeToLastUpdated (eTvDb.getLastUpdated()));

				LocalDate now = LocalDate.now();
				if (firstAired != null && firstAired.isBefore(now)) {
					episode.setViewed(true);
				} else {
					episode.setViewed(false);	
				}
				

				log.debug("episode to save {}", episode);
				episodeRepository.save(episode);
			}

		} catch (TvDbException e) {
			e.printStackTrace();
		}

		return serie;
	}

	private LocalDate parseLocalDateSafe(String s) {
		return StringUtils.isBlank(s) ? null : LocalDate.parse(s);
	}

	private String parseStringSafe(String s) {
		return StringUtils.isBlank(s) ? null : s;
	}
	
	
	private ZonedDateTime convertUnixDateTimeToLastUpdated (String s) {
		if (s == null) 
			return ZonedDateTime.now();
		
		try {
			Long l = Long.valueOf(s);
			
			//Convert to miliseconds
			l = l*1000;
			Date date = new Date(l);
			return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

		} catch (NumberFormatException nfe) {
			return ZonedDateTime.now();
		}		
	}
	
	

	/**
	 * Find series on tvDB by title 
	 * 
	 * @param title
	 * @param language
	 * @return
	 */
	@Timed
	public List<ScratchSeriesDTO> findByTitle(String title, String language) {
		TheTVDBApi tvDB = new TheTVDBApi(tvdbToken);
		log.info("tvdbToken : {} ", tvdbToken);


		List<ScratchSeriesDTO> lScratchSeriesDTO = new LinkedList<>();
		ScratchSeriesDTO oScratchSeriesDTO = null;

		try {
			List<Series> results = tvDB.searchSeries(title, language);
			for (Series s : results) {
				log.info("s : {} ", s);

				oScratchSeriesDTO = new ScratchSeriesDTO();
				oScratchSeriesDTO.setId(parseStringSafe(s.getId()));
				oScratchSeriesDTO.setImdbId(parseStringSafe(s.getImdbId()));
				oScratchSeriesDTO.setOverview(parseStringSafe(s.getOverview()));
				oScratchSeriesDTO.setTitle(parseStringSafe(s.getSeriesName()));
				oScratchSeriesDTO.setUrlBanner(parseStringSafe(s.getBanner()));
				oScratchSeriesDTO.setFirstAired(parseStringSafe(s.getFirstAired()));

				lScratchSeriesDTO.add(oScratchSeriesDTO);

				log.info("oScratchSeriesDTO : {} ", oScratchSeriesDTO);

			}

		} catch (TvDbException e) {
			log.error("Error happen when access TvDb for query '{}' lang '{}' with cause = {}", title, language,
					e.getCause());
			// e.printStackTrace();
		}

		return lScratchSeriesDTO;
	}
	
	

    /**
     * Refresh scratch series to update aired date or title
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     * @throws TvDbException 
     */
	@Scheduled(cron = "0 0 23 * * ?")
    //@Scheduled(fixedRate = 60000)
    @Timed
    public void synchronizeSeriesAndEpisodes() {        
		StatsSincronyzeDTO stats = synchronizeFromTvDb (serieRepository.findAll());
    }

    @Timed
    public StatsSincronyzeDTO synchronizeSeriesAndEpisodesByUserIsCurrentUser() {        
    	StatsSincronyzeDTO stats = synchronizeFromTvDb (serieRepository.findByUserIsCurrentUser());
		return stats;
    }
	
    
    /**
     * Do the synchronization of a list of series
     * 
     * @param serieIterator
     * @return
     */
    private StatsSincronyzeDTO synchronizeFromTvDb(List<Serie> serieIterator) {
    	
    	StatsSincronyzeDTO stats = new StatsSincronyzeDTO();
    	TheTVDBApi tvDB = new TheTVDBApi(tvdbToken);
	    
	    List<Serie> lSeries = serieRepository.findAll();      
	    for (Serie serieLocal : lSeries) {
			if (StringUtils.isBlank(serieLocal.getExternalId())) {
				continue;
			}
			
			log.info("Attend to refresh the Serie {} - {}", serieLocal.getId(), serieLocal.getTitle());		
			Series sTvDB = null;
			try {
				sTvDB = tvDB.getSeries(serieLocal.getExternalId(), "en");
			} catch (TvDbException e) {
				log.error("Error happen when access TvDb to get serie ", e.getCause());		
				continue;
			}
			
			if (needToBeUpdated(serieLocal, sTvDB)) {
				updateSerieFromTvDB (serieLocal, sTvDB);	
				stats.seriesUpdated++;
				serieRepository.save(serieLocal);
			}
				
			//Prepare map of Local Series -  externalId, Episodes  (in case duplicates, just take the first)
			Map<String, com.jtrackseries.domain.Episode> mSerieLocal =
				   serieLocal.getEpisodes().stream()
				   .filter(episode -> episode.getExternalId() != null  )
				   .collect(Collectors.toMap(com.jtrackseries.domain.Episode::getExternalId,
				                             Function.identity(),
				                             (episode1, episode2) -> { return episode1; } )
						   );
				
			log.debug("mSerieLocal : {} ", mSerieLocal);
	
				
			//Iterate the episodes from TvDb and remove from local in case
			List<Episode> lEpisodesTvDB = null;
			
			try {
				lEpisodesTvDB = tvDB.getAllEpisodes(serieLocal.getExternalId(), "en");
			} catch (TvDbException e) {
				log.error("Error happen when access TvDb to getall episodes by Id " + serieLocal.getExternalId(), e.getCause());
				continue;
			}
			
			for (Episode eTvDb : lEpisodesTvDB) {
				log.info("Episode : {} ", eTvDb);					
				if(mSerieLocal != null && mSerieLocal.containsKey(eTvDb.getId()) ) {
					//Check if necessary to update
					com.jtrackseries.domain.Episode episodeLocal = mSerieLocal.get(eTvDb.getId());
					if (needToBeUpdated(episodeLocal, eTvDb)) {
						updateEpisodeFromTvDB(episodeLocal,eTvDb);
						episodeRepository.save(episodeLocal);
						stats.episodesUpdated++;
					}
					
					//remove from local list to evict remove in the phase of purge orphans
					mSerieLocal.remove(eTvDb.getId());

				} else {
					//New Episode to insert in the ddbb
					com.jtrackseries.domain.Episode episodeLocal = new com.jtrackseries.domain.Episode();
					newEpisodeFromTvDB(episodeLocal, eTvDb, serieLocal);
					episodeRepository.save(episodeLocal);
					stats.episodesNewed++;
					
				}
			}
			
			//phase of purge orphans (when in tvDB has been removed, also need to remove in the local ddbb)
			for (com.jtrackseries.domain.Episode episodeLocal : mSerieLocal.values()){
				episodeRepository.delete(episodeLocal);
				stats.episodesRemoved ++;
			}
		}
	    
    	return stats;
    }	
    
    
    /**
     * Updated Always : imdbId, status and lastUpdated from TvDB.
     * Updated in case is empty the Description.
     * 
     * @param serieLocal
     * @param sTvDB
     */
    private void updateSerieFromTvDB (Serie serieLocal, Series sTvDB) {
    	
    	if (serieLocal.getDescription() == null) {
    		serieLocal.setDescription(StringUtils.left(parseStringSafe(sTvDB.getOverview()),2048));	
    	}

    	serieLocal.setImdbId(parseStringSafe(sTvDB.getImdbId()));
    	serieLocal.setStatus(parseStringSafe(sTvDB.getStatus()));
    	serieLocal.setLastUpdated(convertUnixDateTimeToLastUpdated (sTvDB.getLastUpdated()));

		log.info("serie updated {}", serieLocal);		
    }
    

	private boolean needToBeUpdated(Serie serieLocal, Series sTvDB) {
		
		ZonedDateTime lastUpdatedFromTvDB = convertUnixDateTimeToLastUpdated (sTvDB.getLastUpdated());
		ZonedDateTime lastUpdatedFromLocalServer = serieLocal.getLastUpdated();
				
		log.info("needToBeUpdated {} - LastUpdated from localServer {} with TvDB {} ", 
				serieLocal.getTitle(), lastUpdatedFromLocalServer, lastUpdatedFromTvDB);		

		return lastUpdatedFromLocalServer == null ||  lastUpdatedFromLocalServer.isBefore(lastUpdatedFromTvDB);
	}
	
	


	private boolean needToBeUpdated(com.jtrackseries.domain.Episode episodeLocal, Episode eTvDB) {
		
		ZonedDateTime lastUpdatedFromTvDB = convertUnixDateTimeToLastUpdated (eTvDB.getLastUpdated());
		ZonedDateTime lastUpdatedFromLocalServer = episodeLocal.getLastUpdated();
				
		log.info("needToBeUpdated {} - LastUpdated from localServer {} with TvDB {} ", 
				episodeLocal.getTitle(), lastUpdatedFromLocalServer, lastUpdatedFromTvDB);		

		return lastUpdatedFromLocalServer == null ||  lastUpdatedFromLocalServer.isBefore(lastUpdatedFromTvDB);
	}
	

	/**
     * Updated Always : date publish, episodeNumber, season, title and lastUpdated from TvDB.
     * Updated in case is empty the Description.
     * 
	 * @param episodeLocal
	 * @param eTvDB
	 */
	private void updateEpisodeFromTvDB (com.jtrackseries.domain.Episode episodeLocal, Episode eTvDB) {

    	if (episodeLocal.getDescription() == null) {
    		episodeLocal.setDescription(parseStringSafe(eTvDB.getOverview()));	
    	}
    	
		episodeLocal.setDatePublish(parseLocalDateSafe(eTvDB.getFirstAired()));
		episodeLocal.setEpisodeNumber(eTvDB.getEpisodeNumber());		
		episodeLocal.setSeason(String.valueOf(eTvDB.getSeasonNumber()));
		episodeLocal.setTitle(StringUtils.left(StringUtils.defaultString(parseStringSafe(eTvDB.getEpisodeName()), "NOT DEFINED"),2048));				
		episodeLocal.setLastUpdated(convertUnixDateTimeToLastUpdated (eTvDB.getLastUpdated()));

		log.info("episode to update {}", episodeLocal);
	}
	
	
	private void newEpisodeFromTvDB (com.jtrackseries.domain.Episode episodeLocal, Episode eTvDB, Serie serieLocal) {

		LocalDate firstAired = parseLocalDateSafe(eTvDB.getFirstAired());
		episodeLocal.setDatePublish(firstAired);
		episodeLocal.setDescription(StringUtils.left(parseStringSafe(eTvDB.getOverview()),2048));
		episodeLocal.setEpisodeNumber(eTvDB.getEpisodeNumber());
		episodeLocal.setExternalId(parseStringSafe(eTvDB.getId()));
		episodeLocal.setSeason(String.valueOf(eTvDB.getSeasonNumber()));
		episodeLocal.setSerie(serieLocal);
		episodeLocal.setTitle(StringUtils.left(StringUtils.defaultString(parseStringSafe(eTvDB.getEpisodeName()), "NOT DEFINED"),2048));				
		episodeLocal.setLastUpdated(convertUnixDateTimeToLastUpdated (eTvDB.getLastUpdated()));

		LocalDate now = LocalDate.now();
		if (firstAired != null && firstAired.isBefore(now)) {
			episodeLocal.setViewed(true);
		} else {
			episodeLocal.setViewed(false);	
		}	
		log.info("episode to create {}", episodeLocal);

	}
		
}
