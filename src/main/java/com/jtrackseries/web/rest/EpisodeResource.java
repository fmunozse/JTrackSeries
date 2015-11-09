package com.jtrackseries.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jtrackseries.domain.Episode;
import com.jtrackseries.repository.EpisodeRepository;
import com.jtrackseries.web.rest.util.HeaderUtil;
import com.jtrackseries.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
     * POST  /episodes -> Create a new episode.
     */
    @RequestMapping(value = "/episodes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Episode> createEpisode(@Valid @RequestBody Episode episode) throws URISyntaxException {
        log.debug("REST request to save Episode : {}", episode);
        if (episode.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new episode cannot already have an ID").body(null);
        }
        Episode result = episodeRepository.save(episode);
        return ResponseEntity.created(new URI("/api/episodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("episode", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /episodes -> Updates an existing episode.
     */
    @RequestMapping(value = "/episodes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Episode> updateEpisode(@Valid @RequestBody Episode episode) throws URISyntaxException {
        log.debug("REST request to update Episode : {}", episode);
        if (episode.getId() == null) {
            return createEpisode(episode);
        }
        Episode result = episodeRepository.save(episode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("episode", episode.getId().toString()))
            .body(result);
    }

    /**
     * GET  /episodes -> get all the episodes.
     */
    @RequestMapping(value = "/episodes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Episode>> getAllEpisodes(Pageable pageable)
        throws URISyntaxException {
        Page<Episode> page = episodeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/episodes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /episodes/:id -> get the "id" episode.
     */
    @RequestMapping(value = "/episodes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Episode> getEpisode(@PathVariable Long id) {
        log.debug("REST request to get Episode : {}", id);
        return Optional.ofNullable(episodeRepository.findOne(id))
            .map(episode -> new ResponseEntity<>(
                episode,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /episodes/:id -> delete the "id" episode.
     */
    @RequestMapping(value = "/episodes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEpisode(@PathVariable Long id) {
        log.debug("REST request to delete Episode : {}", id);
        episodeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("episode", id.toString())).build();
    }
}
