package com.grasshopper.ngen.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.grasshopper.ngen.domain.Deals;
import com.grasshopper.ngen.service.DealsService;
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
 * REST controller for managing Deals.
 */
@RestController
@RequestMapping("/api")
public class DealsResource {

    private final Logger log = LoggerFactory.getLogger(DealsResource.class);
        
    @Inject
    private DealsService dealsService;
    
    /**
     * POST  /dealss -> Create a new deals.
     */
    @RequestMapping(value = "/dealss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deals> createDeals(@RequestBody Deals deals) throws URISyntaxException {
        log.debug("REST request to save Deals : {}", deals);
        if (deals.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("deals", "idexists", "A new deals cannot already have an ID")).body(null);
        }
        Deals result = dealsService.save(deals);
        return ResponseEntity.created(new URI("/api/dealss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("deals", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dealss -> Updates an existing deals.
     */
    @RequestMapping(value = "/dealss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deals> updateDeals(@RequestBody Deals deals) throws URISyntaxException {
        log.debug("REST request to update Deals : {}", deals);
        if (deals.getId() == null) {
            return createDeals(deals);
        }
        Deals result = dealsService.save(deals);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("deals", deals.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dealss -> get all the dealss.
     */
    @RequestMapping(value = "/dealss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Deals>> getAllDealss(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Dealss");
        Page<Deals> page = dealsService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dealss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dealss/:id -> get the "id" deals.
     */
    @RequestMapping(value = "/dealss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deals> getDeals(@PathVariable Long id) {
        log.debug("REST request to get Deals : {}", id);
        Deals deals = dealsService.findOne(id);
        return Optional.ofNullable(deals)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dealss/:id -> delete the "id" deals.
     */
    @RequestMapping(value = "/dealss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDeals(@PathVariable Long id) {
        log.debug("REST request to delete Deals : {}", id);
        dealsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("deals", id.toString())).build();
    }
}
