package com.ecb.revrecapp.repository;

import com.ecb.revrecapp.domain.RevCycle;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RevCycle entity.
 */
@SuppressWarnings("unused")
public interface RevCycleRepository extends JpaRepository<RevCycle,Long> {

}
