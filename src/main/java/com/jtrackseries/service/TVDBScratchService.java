package com.jtrackseries.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codahale.metrics.annotation.Timed;
import com.jtrackseries.domain.Serie;
import com.jtrackseries.repository.EpisodeRepository;
import com.jtrackseries.repository.SerieRepository;
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
}
