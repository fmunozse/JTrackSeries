package com.jtrackseries.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jtrackseries.domain.ManualTracking;
import com.jtrackseries.repository.ManualTrackingRepository;
import com.jtrackseries.repository.UserRepository;
import com.jtrackseries.security.SecurityUtils;
import com.jtrackseries.web.rest.util.HeaderUtil;

/**
 * REST controller for managing ManualTracking.
 */
@RestController
@RequestMapping("/api")
public class ManualTrackingResource {

    private final Logger log = LoggerFactory.getLogger(ManualTrackingResource.class);
        
    @Inject
    private ManualTrackingRepository manualTrackingRepository;
    
    @Inject 
    private UserRepository userRepository;
    
    /**
     * POST  /manualTrackings -> Create a new manualTracking.
     */
    @RequestMapping(value = "/manualTrackings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManualTracking> createManualTracking(@Valid @RequestBody ManualTracking manualTracking) throws URISyntaxException {
        log.debug("REST request to save ManualTracking : {}", manualTracking);
        if (manualTracking.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("manualTracking", "idexists", "A new manualTracking cannot already have an ID")).body(null);
        }
        manualTracking.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get() );
        ManualTracking result = manualTrackingRepository.save(manualTracking);
        return ResponseEntity.created(new URI("/api/manualTrackings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("manualTracking", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /manualTrackings -> Updates an existing manualTracking.
     */
    @RequestMapping(value = "/manualTrackings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManualTracking> updateManualTracking(@Valid @RequestBody ManualTracking manualTracking) throws URISyntaxException {
        log.debug("REST request to update ManualTracking : {}", manualTracking);
        if (manualTracking.getId() == null) {
            return createManualTracking(manualTracking);
        }
        
        ManualTracking result = manualTrackingRepository.save(manualTracking);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("manualTracking", manualTracking.getId().toString()))
            .body(result);
    }

    /**
     * GET  /manualTrackings -> get all the manualTrackings.
     */
    @RequestMapping(value = "/manualTrackings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ManualTracking> getAllManualTrackings() {
        log.debug("REST request to get all ManualTrackings");
        return manualTrackingRepository.findByUserIsCurrentUser();
            }

    /**
     * GET  /manualTrackings/:id -> get the "id" manualTracking.
     */
    @RequestMapping(value = "/manualTrackings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManualTracking> getManualTracking(@PathVariable Long id) {
        log.debug("REST request to get ManualTracking : {}", id);
        ManualTracking manualTracking = manualTrackingRepository.findOne(id);
        return Optional.ofNullable(manualTracking)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /manualTrackings/:id -> delete the "id" manualTracking.
     */
    @RequestMapping(value = "/manualTrackings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteManualTracking(@PathVariable Long id) {
        log.debug("REST request to delete ManualTracking : {}", id);
        manualTrackingRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("manualTracking", id.toString())).build();
    }
}
