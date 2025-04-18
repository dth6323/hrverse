package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ContractTermination;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContractTermination entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractTerminationRepository extends JpaRepository<ContractTermination, Long> {}
