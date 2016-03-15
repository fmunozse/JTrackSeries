package com.jtrackseries.web.rest;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jtrackseries.domain.Episode;
import com.jtrackseries.repository.EpisodeRepository;

/**
 * REST controller for managing Episode.
 */
@RestController
@RequestMapping("/api")
public class CalendarResource {

	private final Logger log = LoggerFactory.getLogger(CalendarResource.class);

	@Inject
	private EpisodeRepository episodeRepository;

	/**
	 * GET /calendar/episodesByDates/{fromDate}/{toDate} -> get all the episodes
	 * by range of from to date
	 */
	@RequestMapping(value = "/calendar/episodesByDates/{fromDate}/{toDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Episode> getByDates(
			@PathVariable(value = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@PathVariable(value = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

		log.debug("REST request to get a page of Episodes by Range from {} to {} ", fromDate, toDate);

		return episodeRepository.findAllByDatePublishBetween(fromDate, toDate);
	}
}
