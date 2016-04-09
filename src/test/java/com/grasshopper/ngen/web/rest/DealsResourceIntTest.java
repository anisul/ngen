package com.grasshopper.ngen.web.rest;

import com.grasshopper.ngen.Application;
import com.grasshopper.ngen.domain.Deals;
import com.grasshopper.ngen.repository.DealsRepository;
import com.grasshopper.ngen.service.DealsService;

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
 * Test class for the DealsResource REST controller.
 *
 * @see DealsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DealsResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_NOTE = "AAAAA";
    private static final String UPDATED_NOTE = "BBBBB";

    @Inject
    private DealsRepository dealsRepository;

    @Inject
    private DealsService dealsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDealsMockMvc;

    private Deals deals;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DealsResource dealsResource = new DealsResource();
        ReflectionTestUtils.setField(dealsResource, "dealsService", dealsService);
        this.restDealsMockMvc = MockMvcBuilders.standaloneSetup(dealsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        deals = new Deals();
        deals.setTitle(DEFAULT_TITLE);
        deals.setDescription(DEFAULT_DESCRIPTION);
        deals.setStartDate(DEFAULT_START_DATE);
        deals.setEndDate(DEFAULT_END_DATE);
        deals.setNote(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void createDeals() throws Exception {
        int databaseSizeBeforeCreate = dealsRepository.findAll().size();

        // Create the Deals

        restDealsMockMvc.perform(post("/api/dealss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deals)))
                .andExpect(status().isCreated());

        // Validate the Deals in the database
        List<Deals> dealss = dealsRepository.findAll();
        assertThat(dealss).hasSize(databaseSizeBeforeCreate + 1);
        Deals testDeals = dealss.get(dealss.size() - 1);
        assertThat(testDeals.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDeals.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDeals.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDeals.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testDeals.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void getAllDealss() throws Exception {
        // Initialize the database
        dealsRepository.saveAndFlush(deals);

        // Get all the dealss
        restDealsMockMvc.perform(get("/api/dealss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(deals.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }

    @Test
    @Transactional
    public void getDeals() throws Exception {
        // Initialize the database
        dealsRepository.saveAndFlush(deals);

        // Get the deals
        restDealsMockMvc.perform(get("/api/dealss/{id}", deals.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(deals.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDeals() throws Exception {
        // Get the deals
        restDealsMockMvc.perform(get("/api/dealss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeals() throws Exception {
        // Initialize the database
        dealsRepository.saveAndFlush(deals);

		int databaseSizeBeforeUpdate = dealsRepository.findAll().size();

        // Update the deals
        deals.setTitle(UPDATED_TITLE);
        deals.setDescription(UPDATED_DESCRIPTION);
        deals.setStartDate(UPDATED_START_DATE);
        deals.setEndDate(UPDATED_END_DATE);
        deals.setNote(UPDATED_NOTE);

        restDealsMockMvc.perform(put("/api/dealss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deals)))
                .andExpect(status().isOk());

        // Validate the Deals in the database
        List<Deals> dealss = dealsRepository.findAll();
        assertThat(dealss).hasSize(databaseSizeBeforeUpdate);
        Deals testDeals = dealss.get(dealss.size() - 1);
        assertThat(testDeals.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDeals.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDeals.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDeals.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testDeals.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void deleteDeals() throws Exception {
        // Initialize the database
        dealsRepository.saveAndFlush(deals);

		int databaseSizeBeforeDelete = dealsRepository.findAll().size();

        // Get the deals
        restDealsMockMvc.perform(delete("/api/dealss/{id}", deals.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Deals> dealss = dealsRepository.findAll();
        assertThat(dealss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
