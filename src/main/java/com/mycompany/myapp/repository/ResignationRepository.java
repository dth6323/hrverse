package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Resignation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Resignation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResignationRepository extends JpaRepository<Resignation, Long> {}
