package com.grasshopper.ngen.repository;

import com.grasshopper.ngen.domain.Deals;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Deals entity.
 */
public interface DealsRepository extends JpaRepository<Deals,Long> {

}
