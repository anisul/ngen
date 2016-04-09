package com.grasshopper.ngen.service;

import com.grasshopper.ngen.domain.Deals;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Deals.
 */
public interface DealsService {

    /**
     * Save a deals.
     * @return the persisted entity
     */
    public Deals save(Deals deals);

    /**
     *  get all the dealss.
     *  @return the list of entities
     */
    public Page<Deals> findAll(Pageable pageable);

    /**
     *  get the "id" deals.
     *  @return the entity
     */
    public Deals findOne(Long id);

    /**
     *  delete the "id" deals.
     */
    public void delete(Long id);
}
