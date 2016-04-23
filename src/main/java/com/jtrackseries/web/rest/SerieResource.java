package com.jtrackseries.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import com.jtrackseries.domain.Serie;
import com.jtrackseries.repository.SerieRepository;
import com.jtrackseries.service.TVDBScratchService;
import com.jtrackseries.web.rest.dto.StatsSerieSeasonViewedDTO;
import com.jtrackseries.web.rest.dto.StatsSincronyzeDTO;
import com.jtrackseries.web.rest.util.HeaderUtil;
import com.jtrackseries.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Serie.
 */
@RestController
@RequestMapping("/api")
public class SerieResource {

    private final Logger log = LoggerFactory.getLogger(SerieResource.class);
        
    @Inject
    private SerieRepository serieRepository;
    
    @Inject
    private TVDBScratchService oTVDBScratchService;
    


    /**
     * GET  /serie/{id}/hasMoreSeasonThan/{season}
     */
    @RequestMapping(value = "/serie/{id}/hasMoreSeasonThan/{season}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Map<String, Boolean>> hasMoreSeasons(
    		@PathVariable Long id,
    		@PathVariable String season)
    	throws URISyntaxException {
    	
        log.debug("REST request to get if has more season of Series {} of season {} ", id, season);
        Boolean hasSeasson = serieRepository.hasMoreSeason(id,season);
        Map<String, Boolean> m =  Collections.singletonMap("hasSeasson", hasSeasson);

        return new ResponseEntity<>(m,HttpStatus.OK);
    }
    

    /**
     * GET  /serie/statsviewed -> get all the series.
     */
    @RequestMapping(value = "/serie/statsviewed",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<StatsSerieSeasonViewedDTO>>  getStatSeries()
    	throws URISyntaxException {
    	
        log.debug("REST request to last stats of Series");        
        List<StatsSerieSeasonViewedDTO> stats = serieRepository.findLastSeriesStats();   
        
        return new ResponseEntity<>(stats,HttpStatus.OK);
    }
    
    /**
     * GET  /serie/statsviewed/{id} -> get all the series.
     */
    @RequestMapping(value = "/serie/statsviewed/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StatsSerieSeasonViewedDTO> getStatSerieBySeasonAndSerieId(
    		@PathVariable Long id,
    		@RequestParam(value = "season", required = false) Optional<String> season)
    	throws URISyntaxException {
    	
        log.debug("REST request to get stats of Series {} of season {} ", id, season);
        
        StatsSerieSeasonViewedDTO stats = null;
        if (season.isPresent()) {
        	 stats = serieRepository.findOneStatsBySeasonAndSerieId(season.get(), id);        	
        } else {
        	String s = String.valueOf(serieRepository.getMaxSeasonBySerieId(id));
            log.debug("REST max season {} get it", s);
            stats = serieRepository.findOneStatsBySeasonAndSerieId(s, id);    
        }
        
        
        return Optional.ofNullable(stats)
                .map(result -> new ResponseEntity<>(result,HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
	/**
	 * PUT /episodes/{id}/viewed -> Updates an existing episode.
	 * 	param:  set
	 */
	@RequestMapping(value = "/updateAllSeriesFromTvDb", 
			method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<StatsSincronyzeDTO> updateAllSeriesFromTvDb( ) throws URISyntaxException {
		log.debug("REST request to updateAllSeriesFromTvDb " );
		StatsSincronyzeDTO stats = oTVDBScratchService.synchronizeSeriesAndEpisodesByUserIsCurrentUser();

        return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert("Updated your Series From TvDb. " + stats.toString(), "serie"))
                .body(stats);        
	}
	
	
    /**
     * POST  /series -> Create a new serie.
     */
    @RequestMapping(value = "/series",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Serie> createSerie(@Valid @RequestBody Serie serie) throws URISyntaxException {
        log.debug("REST request to save Serie : {}", serie);
        if (serie.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serie", "idexists", "A new serie cannot already have an ID")).body(null);
        }
        Serie result = serieRepository.save(serie);
        return ResponseEntity.created(new URI("/api/series/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serie", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /series -> Updates an existing serie.
     */
    @RequestMapping(value = "/series",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Serie> updateSerie(@Valid @RequestBody Serie serie) throws URISyntaxException {
        log.debug("REST request to update Serie : {}", serie);
        if (serie.getId() == null) {
            return createSerie(serie);
        }
        Serie result = serieRepository.save(serie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serie", serie.getId().toString()))
            .body(result);
    }

    /**
     * GET  /series -> get all the series.
     */
    @RequestMapping(value = "/series",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Serie>> getAllSeries(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Series");
        
        Page<Serie> page = null;            
        page = serieRepository.findByUserIsCurrentUser(pageable); 

        /*
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            page = serieRepository.findByUserIsCurrentUser(pageable); 
        } else {
            page = serieRepository.findAll(pageable); 
        }
        */
        
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/series");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /series/:id -> get the "id" serie.
     */
    @RequestMapping(value = "/series/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Serie> getSerie(@PathVariable Long id) {
        log.debug("REST request to get Serie : {}", id);
        Serie serie = serieRepository.findOne(id);
        return Optional.ofNullable(serie)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /series/:id -> delete the "id" serie.
     */
    @RequestMapping(value = "/series/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSerie(@PathVariable Long id) {
        log.debug("REST request to delete Serie : {}", id);
        serieRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serie", id.toString())).build();
    }
}
