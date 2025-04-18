package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.RewardPunishment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RewardPunishment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RewardPunishmentRepository extends JpaRepository<RewardPunishment, Long> {}
