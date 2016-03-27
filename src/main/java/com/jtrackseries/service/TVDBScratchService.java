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

	@Timed
	public Serie importSeriesById(String id, String language) {
		TheTVDBApi tvDB = new TheTVDBApi("18C5DB503E5170F2");

		Serie serie = new Serie();

		try {
			Series series = tvDB.getSeries(id, language);
			log.info("Series : {} ", series);

			serie.setDescription(parseStringSafe(series.getOverview()));
			serie.setExternalId(parseStringSafe(series.getId()));
			// serie.setExternalLink(externalLink);
			serie.setFirstAired(parseLocalDateSafe(series.getFirstAired()));
			serie.setImdbId(parseStringSafe(series.getImdbId()));
			serie.setStatus(parseStringSafe(series.getStatus()));
			serie.setTitle(parseStringSafe(series.getSeriesName()));			
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
				episode.setDescription(parseStringSafe(eTvDb.getOverview()));
				episode.setEpisodeNumber(eTvDb.getEpisodeNumber());
				episode.setExternalId(parseStringSafe(eTvDb.getId()));
				episode.setSeason(String.valueOf(eTvDb.getSeasonNumber()));
				episode.setSerie(serie);
				episode.setTitle(StringUtils.defaultString(parseStringSafe(eTvDb.getEpisodeName()), "NOT DEFINED"));				
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
		TheTVDBApi tvDB = new TheTVDBApi("18C5DB503E5170F2");

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
	@Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(fixedRate = 60000)
    @Timed
    public void synchronizeSeriesAndEpisodes() {
        ZonedDateTime now = ZonedDateTime.now();
        
    	try {
    		
			TheTVDBApi tvDB = new TheTVDBApi("18C5DB503E5170F2");
	        
	        List<Serie> lSeries = serieRepository.findAll();      
	        for (Serie serieLocal : lSeries) {
				if (StringUtils.isBlank(serieLocal.getExternalId())) {
					continue;
				}
				
				log.info("Attend to refresh the Serie {} - {}", serieLocal.getId(), serieLocal.getTitle());		
				Series sTvDB = tvDB.getSeries(serieLocal.getExternalId(), "en");
				if (needToBeUpdated(serieLocal, sTvDB)) {
					updateSerieFromTvDB (serieLocal, sTvDB);					
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

				
				//Episodes from TvDb
				List<Episode> lEpisodesTvDB = tvDB.getAllEpisodes(serieLocal.getExternalId(), "en");
				for (Episode eTvDb : lEpisodesTvDB) {
					log.info("Episode : {} ", eTvDb);
					
					if(mSerieLocal != null && mSerieLocal.containsKey(eTvDb.getId()) ) {
						//Check if necessary to update
						com.jtrackseries.domain.Episode episodeLocal = mSerieLocal.get(eTvDb.getId());
						if (needToBeUpdated(episodeLocal, eTvDb)) {
							updateEpisodeFromTvDB(episodeLocal,eTvDb);
							episodeRepository.save(episodeLocal);
						}
						
					} else {
						//New Episode to insert in the ddbb
						com.jtrackseries.domain.Episode episodeLocal = new com.jtrackseries.domain.Episode();
						newEpisodeFromTvDB(episodeLocal, eTvDb, serieLocal);
						episodeRepository.save(episodeLocal);
					}
				}
			}
    	
		} catch (TvDbException e) {
			log.error("Error happen when access TvDb",
					e.getCause());
			// e.printStackTrace();
		}

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
    		serieLocal.setDescription(parseStringSafe(sTvDB.getOverview()));	
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
		episodeLocal.setTitle(StringUtils.defaultString(parseStringSafe(eTvDB.getEpisodeName()), "NOT DEFINED"));				
		episodeLocal.setLastUpdated(convertUnixDateTimeToLastUpdated (eTvDB.getLastUpdated()));

		log.info("episode to update {}", episodeLocal);
	}
	
	
	private void newEpisodeFromTvDB (com.jtrackseries.domain.Episode episodeLocal, Episode eTvDB, Serie serieLocal) {

		LocalDate firstAired = parseLocalDateSafe(eTvDB.getFirstAired());
		episodeLocal.setDatePublish(firstAired);
		episodeLocal.setDescription(parseStringSafe(eTvDB.getOverview()));
		episodeLocal.setEpisodeNumber(eTvDB.getEpisodeNumber());
		episodeLocal.setExternalId(parseStringSafe(eTvDB.getId()));
		episodeLocal.setSeason(String.valueOf(eTvDB.getSeasonNumber()));
		episodeLocal.setSerie(serieLocal);
		episodeLocal.setTitle(StringUtils.defaultString(parseStringSafe(eTvDB.getEpisodeName()), "NOT DEFINED"));				
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
