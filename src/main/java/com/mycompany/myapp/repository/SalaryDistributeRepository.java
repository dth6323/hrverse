package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SalaryDistribute;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SalaryDistribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalaryDistributeRepository extends JpaRepository<SalaryDistribute, Long> {}
