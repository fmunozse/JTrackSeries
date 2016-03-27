package com.jtrackseries.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jtrackseries.domain.Episode;
import com.jtrackseries.repository.EpisodeRepository;
import com.jtrackseries.web.rest.util.HeaderUtil;
import com.jtrackseries.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Episode.
 */
@RestController
@RequestMapping("/api")
public class EpisodeResource {

	private final Logger log = LoggerFactory.getLogger(EpisodeResource.class);

	@Inject
	private EpisodeRepository episodeRepository;

	/**
	 * GET /episodes -> get all the episodes.
	 */
	@RequestMapping(value = "/serie/{idSerie}/episodes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Episode>> getAllEpisodesByIdSerie(@PathVariable Long idSerie, Pageable pageable)
			throws URISyntaxException {
		log.debug("REST request to get all of Episodes by Serie Id {}", idSerie);
		Page<Episode> page = episodeRepository.findAllBySerieId(idSerie, pageable);

		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/serie/" + idSerie + "/episodes");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /episodes/{id}/viewed -> Updates an existing episode.
	 * 	param:  set
	 */
	@RequestMapping(value = "/episodes/{id}/viewed", 
			params = { "set" }, 
			method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Episode> updateViewedEpisode(@PathVariable Long id, 
			@RequestParam(value = "set", required = false) Boolean set ) throws URISyntaxException {
		log.debug("REST request to update Episode {} with viewed {}",id, set );

		int result = episodeRepository.setViewed(id, set);

		Episode episode = episodeRepository.findOne(id);
		return Optional.ofNullable(episode).map(resultEp -> new ResponseEntity<>(resultEp,  HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));		
	}
	
	/**
	 * POST /episodes -> Create a new episode.
	 */
	@RequestMapping(value = "/episodes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Episode> createEpisode(@Valid @RequestBody Episode episode) throws URISyntaxException {
		log.debug("REST request to save Episode : {}", episode);
		if (episode.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert("episode", "idexists", "A new episode cannot already have an ID"))
					.body(null);
		}
		Episode result = episodeRepository.save(episode);
		return ResponseEntity.created(new URI("/api/episodes/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("episode", result.getId().toString())).body(result);
	}

	/**
	 * PUT /episodes -> Updates an existing episode.
	 */
	@RequestMapping(value = "/episodes", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Episode> updateEpisode(@Valid @RequestBody Episode episode) throws URISyntaxException {
		log.debug("REST request to update Episode : {}", episode);
		if (episode.getId() == null) {
			return createEpisode(episode);
		}
		Episode result = episodeRepository.save(episode);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("episode", episode.getId().toString()))
				.body(result);
	}

	/**
	 * GET /episodes -> get all the episodes.
	 */
	@RequestMapping(value = "/episodes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Episode>> getAllEpisodes(Pageable pageable) throws URISyntaxException {
		log.debug("REST request to get a page of Episodes");
		Page<Episode> page = episodeRepository.findByUserIsCurrentUser(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/episodes");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
 
	/**
	 * GET /episodes/:id -> get the "id" episode.
	 */
	@RequestMapping(value = "/episodes/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Episode> getEpisode(@PathVariable Long id) {
		log.debug("REST request to get Episode : {}", id);
		Episode episode = episodeRepository.findOne(id);
		return Optional.ofNullable(episode).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /episodes/:id -> delete the "id" episode.
	 */
	@RequestMapping(value = "/episodes/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteEpisode(@PathVariable Long id) {
		log.debug("REST request to delete Episode : {}", id);
		episodeRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("episode", id.toString())).build();
	}
}
