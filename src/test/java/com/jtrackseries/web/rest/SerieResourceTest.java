package com.jtrackseries.web.rest;

import com.jtrackseries.Application;
import com.jtrackseries.domain.Serie;
import com.jtrackseries.repository.SerieRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SerieResource REST controller.
 *
 * @see SerieResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SerieResourceTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_EXTERNAL_LINK = "AAAAA";
    private static final String UPDATED_EXTERNAL_LINK = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    @Inject
    private SerieRepository serieRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSerieMockMvc;

    private Serie serie;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SerieResource serieResource = new SerieResource();
        ReflectionTestUtils.setField(serieResource, "serieRepository", serieRepository);
        this.restSerieMockMvc = MockMvcBuilders.standaloneSetup(serieResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serie = new Serie();
        serie.setTitle(DEFAULT_TITLE);
        serie.setExternalLink(DEFAULT_EXTERNAL_LINK);
        serie.setDescription(DEFAULT_DESCRIPTION);
        serie.setNotes(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createSerie() throws Exception {
        int databaseSizeBeforeCreate = serieRepository.findAll().size();

        // Create the Serie

        restSerieMockMvc.perform(post("/api/series")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serie)))
                .andExpect(status().isCreated());

        // Validate the Serie in the database
        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeCreate + 1);
        Serie testSerie = series.get(series.size() - 1);
        assertThat(testSerie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSerie.getExternalLink()).isEqualTo(DEFAULT_EXTERNAL_LINK);
        assertThat(testSerie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSerie.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = serieRepository.findAll().size();
        // set the field null
        serie.setTitle(null);

        // Create the Serie, which fails.

        restSerieMockMvc.perform(post("/api/series")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serie)))
                .andExpect(status().isBadRequest());

        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSeries() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        // Get all the series
        restSerieMockMvc.perform(get("/api/series"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serie.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].externalLink").value(hasItem(DEFAULT_EXTERNAL_LINK.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

        // Get the serie
        restSerieMockMvc.perform(get("/api/series/{id}", serie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(serie.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.externalLink").value(DEFAULT_EXTERNAL_LINK.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSerie() throws Exception {
        // Get the serie
        restSerieMockMvc.perform(get("/api/series/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

		int databaseSizeBeforeUpdate = serieRepository.findAll().size();

        // Update the serie
        serie.setTitle(UPDATED_TITLE);
        serie.setExternalLink(UPDATED_EXTERNAL_LINK);
        serie.setDescription(UPDATED_DESCRIPTION);
        serie.setNotes(UPDATED_NOTES);

        restSerieMockMvc.perform(put("/api/series")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serie)))
                .andExpect(status().isOk());

        // Validate the Serie in the database
        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeUpdate);
        Serie testSerie = series.get(series.size() - 1);
        assertThat(testSerie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSerie.getExternalLink()).isEqualTo(UPDATED_EXTERNAL_LINK);
        assertThat(testSerie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSerie.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void deleteSerie() throws Exception {
        // Initialize the database
        serieRepository.saveAndFlush(serie);

		int databaseSizeBeforeDelete = serieRepository.findAll().size();

        // Get the serie
        restSerieMockMvc.perform(delete("/api/series/{id}", serie.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Serie> series = serieRepository.findAll();
        assertThat(series).hasSize(databaseSizeBeforeDelete - 1);
    }
}
