package com.jtrackseries.service;

import static org.assertj.core.api.StrictAssertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.jtrackseries.Application;
import com.jtrackseries.domain.Serie;
import com.jtrackseries.repository.SerieRepository;
import com.jtrackseries.web.rest.dto.ScratchSeriesDTO;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class TVDBScratchServiceIntTest {

	private final Logger log = LoggerFactory.getLogger(TVDBScratchService.class);

	@Inject
	private TVDBScratchService service;

	@Inject
	private SerieRepository serieRepository;

	@Test
	public void assertFindByTitleIsWorking() {
		List<ScratchSeriesDTO> lScratchSeriesDTO = service.findByTitle("Fargo", null);
		assertThat(lScratchSeriesDTO).isNotNull();
	}

	@Test
	public void assertImportIsWorking() {
		service.importSeriesById("269613", null);

		List<Serie> lSeries = serieRepository.findAllByTitle("Fargo");
		log.info("result {} ", lSeries);
		assertThat(lSeries).isNotNull();

	}
}
