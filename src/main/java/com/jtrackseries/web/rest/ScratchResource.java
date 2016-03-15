package com.jtrackseries.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jtrackseries.domain.Serie;
import com.jtrackseries.service.TVDBScratchService;
import com.jtrackseries.web.rest.dto.ScratchSeriesDTO;
import com.jtrackseries.web.rest.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class ScratchResource {

	private final Logger log = LoggerFactory.getLogger(ScratchResource.class);

	@Inject
	private TVDBScratchService oTVDBScratchService;

	/**
	 * GET /scratch/serie/{title} ->
	 */
	@RequestMapping(value = "/scratch/serie/{title}", method = RequestMethod.GET, //
			params = { "lan" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<ScratchSeriesDTO> findSeriesByTitle(@PathVariable(value = "title") String title,
			@RequestParam(value = "lan", required = false) Optional<String> lan) {

		log.debug("REST request to find series {} , using the lan {} ", title, lan);

		return oTVDBScratchService.findByTitle(title, lan.orElse(null));
	}

	/**
	 * GET /scratch/importSerieById/{id:.+}" ->
	 * 
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/scratch/importSerieById/{id:.+}", method = RequestMethod.GET, //
			params = { "lan" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Serie> importSerieById(@PathVariable(value = "id") String id,
			@RequestParam(value = "lan", required = false) Optional<String> lan) throws URISyntaxException {

		log.debug("REST request to import series by id {} , using the lan {} ", id, lan);
		Serie result = oTVDBScratchService.importSeriesById(id, lan.orElse(null));

		return ResponseEntity.created(new URI("/api/episodes/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("episode", result.getId().toString())).body(result);

	}

}
