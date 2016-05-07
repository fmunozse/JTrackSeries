package com.jtrackseries.web.rest;

import com.jtrackseries.Application;
import com.jtrackseries.domain.ManualTracking;
import com.jtrackseries.repository.ManualTrackingRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ManualTrackingResource REST controller.
 *
 * @see ManualTrackingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ManualTrackingResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_SEASON = "AAAAA";
    private static final String UPDATED_SEASON = "BBBBB";

    private static final Integer DEFAULT_TOTAL_EPISODES = 1;
    private static final Integer UPDATED_TOTAL_EPISODES = 2;

    private static final Integer DEFAULT_LAST_VIEWED = 1;
    private static final Integer UPDATED_LAST_VIEWED = 2;

    private static final LocalDate DEFAULT_DATE_REMAINDER = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_REMAINDER = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ManualTrackingRepository manualTrackingRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restManualTrackingMockMvc;

    private ManualTracking manualTracking;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ManualTrackingResource manualTrackingResource = new ManualTrackingResource();
        ReflectionTestUtils.setField(manualTrackingResource, "manualTrackingRepository", manualTrackingRepository);
        this.restManualTrackingMockMvc = MockMvcBuilders.standaloneSetup(manualTrackingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        manualTracking = new ManualTracking();
        manualTracking.setTitle(DEFAULT_TITLE);
        manualTracking.setSeason(DEFAULT_SEASON);
        manualTracking.setTotalEpisodes(DEFAULT_TOTAL_EPISODES);
        manualTracking.setLastViewed(DEFAULT_LAST_VIEWED);
        manualTracking.setDateRemainder(DEFAULT_DATE_REMAINDER);
    }

    @Test
    @Transactional
    public void createManualTracking() throws Exception {
        int databaseSizeBeforeCreate = manualTrackingRepository.findAll().size();

        // Create the ManualTracking

        restManualTrackingMockMvc.perform(post("/api/manualTrackings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(manualTracking)))
                .andExpect(status().isCreated());

        // Validate the ManualTracking in the database
        List<ManualTracking> manualTrackings = manualTrackingRepository.findAll();
        assertThat(manualTrackings).hasSize(databaseSizeBeforeCreate + 1);
        ManualTracking testManualTracking = manualTrackings.get(manualTrackings.size() - 1);
        assertThat(testManualTracking.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testManualTracking.getSeason()).isEqualTo(DEFAULT_SEASON);
        assertThat(testManualTracking.getTotalEpisodes()).isEqualTo(DEFAULT_TOTAL_EPISODES);
        assertThat(testManualTracking.getLastViewed()).isEqualTo(DEFAULT_LAST_VIEWED);
        assertThat(testManualTracking.getDateRemainder()).isEqualTo(DEFAULT_DATE_REMAINDER);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = manualTrackingRepository.findAll().size();
        // set the field null
        manualTracking.setTitle(null);

        // Create the ManualTracking, which fails.

        restManualTrackingMockMvc.perform(post("/api/manualTrackings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(manualTracking)))
                .andExpect(status().isBadRequest());

        List<ManualTracking> manualTrackings = manualTrackingRepository.findAll();
        assertThat(manualTrackings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllManualTrackings() throws Exception {
        // Initialize the database
        manualTrackingRepository.saveAndFlush(manualTracking);

        // Get all the manualTrackings
        restManualTrackingMockMvc.perform(get("/api/manualTrackings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(manualTracking.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].season").value(hasItem(DEFAULT_SEASON.toString())))
                .andExpect(jsonPath("$.[*].totalEpisodes").value(hasItem(DEFAULT_TOTAL_EPISODES)))
                .andExpect(jsonPath("$.[*].lastViewed").value(hasItem(DEFAULT_LAST_VIEWED)))
                .andExpect(jsonPath("$.[*].dateRemainder").value(hasItem(DEFAULT_DATE_REMAINDER.toString())));
    }

    @Test
    @Transactional
    public void getManualTracking() throws Exception {
        // Initialize the database
        manualTrackingRepository.saveAndFlush(manualTracking);

        // Get the manualTracking
        restManualTrackingMockMvc.perform(get("/api/manualTrackings/{id}", manualTracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(manualTracking.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.season").value(DEFAULT_SEASON.toString()))
            .andExpect(jsonPath("$.totalEpisodes").value(DEFAULT_TOTAL_EPISODES))
            .andExpect(jsonPath("$.lastViewed").value(DEFAULT_LAST_VIEWED))
            .andExpect(jsonPath("$.dateRemainder").value(DEFAULT_DATE_REMAINDER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingManualTracking() throws Exception {
        // Get the manualTracking
        restManualTrackingMockMvc.perform(get("/api/manualTrackings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateManualTracking() throws Exception {
        // Initialize the database
        manualTrackingRepository.saveAndFlush(manualTracking);

		int databaseSizeBeforeUpdate = manualTrackingRepository.findAll().size();

        // Update the manualTracking
        manualTracking.setTitle(UPDATED_TITLE);
        manualTracking.setSeason(UPDATED_SEASON);
        manualTracking.setTotalEpisodes(UPDATED_TOTAL_EPISODES);
        manualTracking.setLastViewed(UPDATED_LAST_VIEWED);
        manualTracking.setDateRemainder(UPDATED_DATE_REMAINDER);

        restManualTrackingMockMvc.perform(put("/api/manualTrackings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(manualTracking)))
                .andExpect(status().isOk());

        // Validate the ManualTracking in the database
        List<ManualTracking> manualTrackings = manualTrackingRepository.findAll();
        assertThat(manualTrackings).hasSize(databaseSizeBeforeUpdate);
        ManualTracking testManualTracking = manualTrackings.get(manualTrackings.size() - 1);
        assertThat(testManualTracking.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testManualTracking.getSeason()).isEqualTo(UPDATED_SEASON);
        assertThat(testManualTracking.getTotalEpisodes()).isEqualTo(UPDATED_TOTAL_EPISODES);
        assertThat(testManualTracking.getLastViewed()).isEqualTo(UPDATED_LAST_VIEWED);
        assertThat(testManualTracking.getDateRemainder()).isEqualTo(UPDATED_DATE_REMAINDER);
    }

    @Test
    @Transactional
    public void deleteManualTracking() throws Exception {
        // Initialize the database
        manualTrackingRepository.saveAndFlush(manualTracking);

		int databaseSizeBeforeDelete = manualTrackingRepository.findAll().size();

        // Get the manualTracking
        restManualTrackingMockMvc.perform(delete("/api/manualTrackings/{id}", manualTracking.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ManualTracking> manualTrackings = manualTrackingRepository.findAll();
        assertThat(manualTrackings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
