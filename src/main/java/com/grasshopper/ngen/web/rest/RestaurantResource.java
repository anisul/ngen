package com.grasshopper.ngen.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.grasshopper.ngen.domain.Restaurant;
import com.grasshopper.ngen.service.RestaurantService;
import com.grasshopper.ngen.web.rest.util.HeaderUtil;
import com.grasshopper.ngen.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Restaurant.
 */
@RestController
@RequestMapping("/api")
public class RestaurantResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantResource.class);
        
    @Inject
    private RestaurantService restaurantService;
    
    /**
     * POST  /restaurants -> Create a new restaurant.
     */
    @RequestMapping(value = "/restaurants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) throws URISyntaxException {
        log.debug("REST request to save Restaurant : {}", restaurant);
        if (restaurant.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("restaurant", "idexists", "A new restaurant cannot already have an ID")).body(null);
        }
        Restaurant result = restaurantService.save(restaurant);
        return ResponseEntity.created(new URI("/api/restaurants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("restaurant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /restaurants -> Updates an existing restaurant.
     */
    @RequestMapping(value = "/restaurants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Restaurant> updateRestaurant(@RequestBody Restaurant restaurant) throws URISyntaxException {
        log.debug("REST request to update Restaurant : {}", restaurant);
        if (restaurant.getId() == null) {
            return createRestaurant(restaurant);
        }
        Restaurant result = restaurantService.save(restaurant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("restaurant", restaurant.getId().toString()))
            .body(result);
    }

    /**
     * GET  /restaurants -> get all the restaurants.
     */
    @RequestMapping(value = "/restaurants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Restaurant>> getAllRestaurants(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Restaurants");
        Page<Restaurant> page = restaurantService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/restaurants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /restaurants/:id -> get the "id" restaurant.
     */
    @RequestMapping(value = "/restaurants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Long id) {
        log.debug("REST request to get Restaurant : {}", id);
        Restaurant restaurant = restaurantService.findOne(id);
        return Optional.ofNullable(restaurant)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /restaurants/:id -> delete the "id" restaurant.
     */
    @RequestMapping(value = "/restaurants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        log.debug("REST request to delete Restaurant : {}", id);
        restaurantService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("restaurant", id.toString())).build();
    }
}
