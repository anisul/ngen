package com.grasshopper.ngen.service.impl;

import com.grasshopper.ngen.service.RestaurantService;
import com.grasshopper.ngen.domain.Restaurant;
import com.grasshopper.ngen.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Restaurant.
 */
@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService{

    private final Logger log = LoggerFactory.getLogger(RestaurantServiceImpl.class);
    
    @Inject
    private RestaurantRepository restaurantRepository;
    
    /**
     * Save a restaurant.
     * @return the persisted entity
     */
    public Restaurant save(Restaurant restaurant) {
        log.debug("Request to save Restaurant : {}", restaurant);
        Restaurant result = restaurantRepository.save(restaurant);
        return result;
    }

    /**
     *  get all the restaurants.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Restaurant> findAll(Pageable pageable) {
        log.debug("Request to get all Restaurants");
        Page<Restaurant> result = restaurantRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one restaurant by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Restaurant findOne(Long id) {
        log.debug("Request to get Restaurant : {}", id);
        Restaurant restaurant = restaurantRepository.findOne(id);
        return restaurant;
    }

    /**
     *  delete the  restaurant by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Restaurant : {}", id);
        restaurantRepository.delete(id);
    }
}
