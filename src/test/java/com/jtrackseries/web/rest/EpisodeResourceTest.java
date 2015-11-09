package com.jtrackseries.web.rest;

import com.jtrackseries.Application;
import com.jtrackseries.domain.Episode;
import com.jtrackseries.repository.EpisodeRepository;

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
 * Test class for the EpisodeResource REST controller.
 *
 * @see EpisodeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EpisodeResourceTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final LocalDate DEFAULT_DATE_PUBLISH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PUBLISH = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_VIEWED = false;
    private static final Boolean UPDATED_VIEWED = true;

    @Inject
    private EpisodeRepository episodeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEpisodeMockMvc;

    private Episode episode;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EpisodeResource episodeResource = new EpisodeResource();
        ReflectionTestUtils.setField(episodeResource, "episodeRepository", episodeRepository);
        this.restEpisodeMockMvc = MockMvcBuilders.standaloneSetup(episodeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        episode = new Episode();
        episode.setTitle(DEFAULT_TITLE);
        episode.setDatePublish(DEFAULT_DATE_PUBLISH);
        episode.setViewed(DEFAULT_VIEWED);
    }

    @Test
    @Transactional
    public void createEpisode() throws Exception {
        int databaseSizeBeforeCreate = episodeRepository.findAll().size();

        // Create the Episode

        restEpisodeMockMvc.perform(post("/api/episodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(episode)))
                .andExpect(status().isCreated());

        // Validate the Episode in the database
        List<Episode> episodes = episodeRepository.findAll();
        assertThat(episodes).hasSize(databaseSizeBeforeCreate + 1);
        Episode testEpisode = episodes.get(episodes.size() - 1);
        assertThat(testEpisode.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEpisode.getDatePublish()).isEqualTo(DEFAULT_DATE_PUBLISH);
        assertThat(testEpisode.getViewed()).isEqualTo(DEFAULT_VIEWED);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = episodeRepository.findAll().size();
        // set the field null
        episode.setTitle(null);

        // Create the Episode, which fails.

        restEpisodeMockMvc.perform(post("/api/episodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(episode)))
                .andExpect(status().isBadRequest());

        List<Episode> episodes = episodeRepository.findAll();
        assertThat(episodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkViewedIsRequired() throws Exception {
        int databaseSizeBeforeTest = episodeRepository.findAll().size();
        // set the field null
        episode.setViewed(null);

        // Create the Episode, which fails.

        restEpisodeMockMvc.perform(post("/api/episodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(episode)))
                .andExpect(status().isBadRequest());

        List<Episode> episodes = episodeRepository.findAll();
        assertThat(episodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEpisodes() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get all the episodes
        restEpisodeMockMvc.perform(get("/api/episodes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(episode.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].datePublish").value(hasItem(DEFAULT_DATE_PUBLISH.toString())))
                .andExpect(jsonPath("$.[*].viewed").value(hasItem(DEFAULT_VIEWED.booleanValue())));
    }

    @Test
    @Transactional
    public void getEpisode() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

        // Get the episode
        restEpisodeMockMvc.perform(get("/api/episodes/{id}", episode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(episode.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.datePublish").value(DEFAULT_DATE_PUBLISH.toString()))
            .andExpect(jsonPath("$.viewed").value(DEFAULT_VIEWED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEpisode() throws Exception {
        // Get the episode
        restEpisodeMockMvc.perform(get("/api/episodes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEpisode() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

		int databaseSizeBeforeUpdate = episodeRepository.findAll().size();

        // Update the episode
        episode.setTitle(UPDATED_TITLE);
        episode.setDatePublish(UPDATED_DATE_PUBLISH);
        episode.setViewed(UPDATED_VIEWED);

        restEpisodeMockMvc.perform(put("/api/episodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(episode)))
                .andExpect(status().isOk());

        // Validate the Episode in the database
        List<Episode> episodes = episodeRepository.findAll();
        assertThat(episodes).hasSize(databaseSizeBeforeUpdate);
        Episode testEpisode = episodes.get(episodes.size() - 1);
        assertThat(testEpisode.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEpisode.getDatePublish()).isEqualTo(UPDATED_DATE_PUBLISH);
        assertThat(testEpisode.getViewed()).isEqualTo(UPDATED_VIEWED);
    }

    @Test
    @Transactional
    public void deleteEpisode() throws Exception {
        // Initialize the database
        episodeRepository.saveAndFlush(episode);

		int databaseSizeBeforeDelete = episodeRepository.findAll().size();

        // Get the episode
        restEpisodeMockMvc.perform(delete("/api/episodes/{id}", episode.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Episode> episodes = episodeRepository.findAll();
        assertThat(episodes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
