package com.grasshopper.ngen.service.impl;

import com.grasshopper.ngen.service.DealsService;
import com.grasshopper.ngen.domain.Deals;
import com.grasshopper.ngen.repository.DealsRepository;
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
 * Service Implementation for managing Deals.
 */
@Service
@Transactional
public class DealsServiceImpl implements DealsService{

    private final Logger log = LoggerFactory.getLogger(DealsServiceImpl.class);
    
    @Inject
    private DealsRepository dealsRepository;
    
    /**
     * Save a deals.
     * @return the persisted entity
     */
    public Deals save(Deals deals) {
        log.debug("Request to save Deals : {}", deals);
        Deals result = dealsRepository.save(deals);
        return result;
    }

    /**
     *  get all the dealss.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Deals> findAll(Pageable pageable) {
        log.debug("Request to get all Dealss");
        Page<Deals> result = dealsRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one deals by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Deals findOne(Long id) {
        log.debug("Request to get Deals : {}", id);
        Deals deals = dealsRepository.findOne(id);
        return deals;
    }

    /**
     *  delete the  deals by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Deals : {}", id);
        dealsRepository.delete(id);
    }
}
