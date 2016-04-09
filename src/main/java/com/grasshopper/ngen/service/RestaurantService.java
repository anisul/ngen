package com.grasshopper.ngen.service;

import com.grasshopper.ngen.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Restaurant.
 */
public interface RestaurantService {

    /**
     * Save a restaurant.
     * @return the persisted entity
     */
    public Restaurant save(Restaurant restaurant);

    /**
     *  get all the restaurants.
     *  @return the list of entities
     */
    public Page<Restaurant> findAll(Pageable pageable);

    /**
     *  get the "id" restaurant.
     *  @return the entity
     */
    public Restaurant findOne(Long id);

    /**
     *  delete the "id" restaurant.
     */
    public void delete(Long id);
}
