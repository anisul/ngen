package com.grasshopper.ngen.web.rest;

import com.grasshopper.ngen.Application;
import com.grasshopper.ngen.domain.Restaurant;
import com.grasshopper.ngen.repository.RestaurantRepository;
import com.grasshopper.ngen.service.RestaurantService;

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

import com.grasshopper.ngen.domain.enumeration.RestaurantType;

/**
 * Test class for the RestaurantResource REST controller.
 *
 * @see RestaurantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RestaurantResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";
    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";

    private static final Boolean DEFAULT_VERIFIED = false;
    private static final Boolean UPDATED_VERIFIED = true;
    private static final String DEFAULT_BANNER_FILE = "AAAAA";
    private static final String UPDATED_BANNER_FILE = "BBBBB";
    private static final String DEFAULT_ABOUT = "AAAAA";
    private static final String UPDATED_ABOUT = "BBBBB";
    
    private static final RestaurantType DEFAULT_CATEGORY = RestaurantType.Chinese;
    private static final RestaurantType UPDATED_CATEGORY = RestaurantType.Thai;
    private static final String DEFAULT_GMAP_CODE = "AAAAA";
    private static final String UPDATED_GMAP_CODE = "BBBBB";

    @Inject
    private RestaurantRepository restaurantRepository;

    @Inject
    private RestaurantService restaurantService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRestaurantMockMvc;

    private Restaurant restaurant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RestaurantResource restaurantResource = new RestaurantResource();
        ReflectionTestUtils.setField(restaurantResource, "restaurantService", restaurantService);
        this.restRestaurantMockMvc = MockMvcBuilders.standaloneSetup(restaurantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        restaurant = new Restaurant();
        restaurant.setName(DEFAULT_NAME);
        restaurant.setAddress(DEFAULT_ADDRESS);
        restaurant.setPhone(DEFAULT_PHONE);
        restaurant.setVerified(DEFAULT_VERIFIED);
        restaurant.setBannerFile(DEFAULT_BANNER_FILE);
        restaurant.setAbout(DEFAULT_ABOUT);
        restaurant.setCategory(DEFAULT_CATEGORY);
        restaurant.setGmapCode(DEFAULT_GMAP_CODE);
    }

    @Test
    @Transactional
    public void createRestaurant() throws Exception {
        int databaseSizeBeforeCreate = restaurantRepository.findAll().size();

        // Create the Restaurant

        restRestaurantMockMvc.perform(post("/api/restaurants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(restaurant)))
                .andExpect(status().isCreated());

        // Validate the Restaurant in the database
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertThat(restaurants).hasSize(databaseSizeBeforeCreate + 1);
        Restaurant testRestaurant = restaurants.get(restaurants.size() - 1);
        assertThat(testRestaurant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRestaurant.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testRestaurant.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testRestaurant.getVerified()).isEqualTo(DEFAULT_VERIFIED);
        assertThat(testRestaurant.getBannerFile()).isEqualTo(DEFAULT_BANNER_FILE);
        assertThat(testRestaurant.getAbout()).isEqualTo(DEFAULT_ABOUT);
        assertThat(testRestaurant.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testRestaurant.getGmapCode()).isEqualTo(DEFAULT_GMAP_CODE);
    }

    @Test
    @Transactional
    public void getAllRestaurants() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurants
        restRestaurantMockMvc.perform(get("/api/restaurants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(restaurant.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED.booleanValue())))
                .andExpect(jsonPath("$.[*].bannerFile").value(hasItem(DEFAULT_BANNER_FILE.toString())))
                .andExpect(jsonPath("$.[*].about").value(hasItem(DEFAULT_ABOUT.toString())))
                .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
                .andExpect(jsonPath("$.[*].gmapCode").value(hasItem(DEFAULT_GMAP_CODE.toString())));
    }

    @Test
    @Transactional
    public void getRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get the restaurant
        restRestaurantMockMvc.perform(get("/api/restaurants/{id}", restaurant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(restaurant.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.verified").value(DEFAULT_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.bannerFile").value(DEFAULT_BANNER_FILE.toString()))
            .andExpect(jsonPath("$.about").value(DEFAULT_ABOUT.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.gmapCode").value(DEFAULT_GMAP_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRestaurant() throws Exception {
        // Get the restaurant
        restRestaurantMockMvc.perform(get("/api/restaurants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

		int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();

        // Update the restaurant
        restaurant.setName(UPDATED_NAME);
        restaurant.setAddress(UPDATED_ADDRESS);
        restaurant.setPhone(UPDATED_PHONE);
        restaurant.setVerified(UPDATED_VERIFIED);
        restaurant.setBannerFile(UPDATED_BANNER_FILE);
        restaurant.setAbout(UPDATED_ABOUT);
        restaurant.setCategory(UPDATED_CATEGORY);
        restaurant.setGmapCode(UPDATED_GMAP_CODE);

        restRestaurantMockMvc.perform(put("/api/restaurants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(restaurant)))
                .andExpect(status().isOk());

        // Validate the Restaurant in the database
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertThat(restaurants).hasSize(databaseSizeBeforeUpdate);
        Restaurant testRestaurant = restaurants.get(restaurants.size() - 1);
        assertThat(testRestaurant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRestaurant.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testRestaurant.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testRestaurant.getVerified()).isEqualTo(UPDATED_VERIFIED);
        assertThat(testRestaurant.getBannerFile()).isEqualTo(UPDATED_BANNER_FILE);
        assertThat(testRestaurant.getAbout()).isEqualTo(UPDATED_ABOUT);
        assertThat(testRestaurant.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testRestaurant.getGmapCode()).isEqualTo(UPDATED_GMAP_CODE);
    }

    @Test
    @Transactional
    public void deleteRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

		int databaseSizeBeforeDelete = restaurantRepository.findAll().size();

        // Get the restaurant
        restRestaurantMockMvc.perform(delete("/api/restaurants/{id}", restaurant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertThat(restaurants).hasSize(databaseSizeBeforeDelete - 1);
    }
}
