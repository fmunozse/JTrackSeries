package com.jtrackseries.web.rest;

import com.jtrackseries.Application;
import com.jtrackseries.domain.Season;
import com.jtrackseries.repository.SeasonRepository;

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
 * Test class for the SeasonResource REST controller.
 *
 * @see SeasonResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SeasonResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final Integer DEFAULT_ORDER_NUMBER = 1;
    private static final Integer UPDATED_ORDER_NUMBER = 2;
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    @Inject
    private SeasonRepository seasonRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSeasonMockMvc;

    private Season season;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SeasonResource seasonResource = new SeasonResource();
        ReflectionTestUtils.setField(seasonResource, "seasonRepository", seasonRepository);
        this.restSeasonMockMvc = MockMvcBuilders.standaloneSetup(seasonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        season = new Season();
        season.setTitle(DEFAULT_TITLE);
        season.setOrderNumber(DEFAULT_ORDER_NUMBER);
        season.setNotes(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void createSeason() throws Exception {
        int databaseSizeBeforeCreate = seasonRepository.findAll().size();

        // Create the Season

        restSeasonMockMvc.perform(post("/api/seasons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(season)))
                .andExpect(status().isCreated());

        // Validate the Season in the database
        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons).hasSize(databaseSizeBeforeCreate + 1);
        Season testSeason = seasons.get(seasons.size() - 1);
        assertThat(testSeason.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSeason.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testSeason.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = seasonRepository.findAll().size();
        // set the field null
        season.setTitle(null);

        // Create the Season, which fails.

        restSeasonMockMvc.perform(post("/api/seasons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(season)))
                .andExpect(status().isBadRequest());

        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = seasonRepository.findAll().size();
        // set the field null
        season.setOrderNumber(null);

        // Create the Season, which fails.

        restSeasonMockMvc.perform(post("/api/seasons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(season)))
                .andExpect(status().isBadRequest());

        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSeasons() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get all the seasons
        restSeasonMockMvc.perform(get("/api/seasons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(season.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getSeason() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

        // Get the season
        restSeasonMockMvc.perform(get("/api/seasons/{id}", season.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(season.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSeason() throws Exception {
        // Get the season
        restSeasonMockMvc.perform(get("/api/seasons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSeason() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

		int databaseSizeBeforeUpdate = seasonRepository.findAll().size();

        // Update the season
        season.setTitle(UPDATED_TITLE);
        season.setOrderNumber(UPDATED_ORDER_NUMBER);
        season.setNotes(UPDATED_NOTES);

        restSeasonMockMvc.perform(put("/api/seasons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(season)))
                .andExpect(status().isOk());

        // Validate the Season in the database
        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons).hasSize(databaseSizeBeforeUpdate);
        Season testSeason = seasons.get(seasons.size() - 1);
        assertThat(testSeason.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSeason.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testSeason.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    public void deleteSeason() throws Exception {
        // Initialize the database
        seasonRepository.saveAndFlush(season);

		int databaseSizeBeforeDelete = seasonRepository.findAll().size();

        // Get the season
        restSeasonMockMvc.perform(delete("/api/seasons/{id}", season.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Season> seasons = seasonRepository.findAll();
        assertThat(seasons).hasSize(databaseSizeBeforeDelete - 1);
    }
}
