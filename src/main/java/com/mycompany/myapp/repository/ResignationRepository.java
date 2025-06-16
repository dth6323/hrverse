package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Resignation;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Resignation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResignationRepository extends JpaRepository<Resignation, Long> {
    @Query(
        value = """
            Select r.* from Resignation r
            join employee e on r.employee_id = e.id
            join jhi_user u on u.id = e.user_id
            where u.login = :login
        """,
        nativeQuery = true
    )
    Page<Resignation> getResignationlogin(@Param("login") String login, Pageable pageable);

    @Query(
        value = """
        SELECT COUNT(*) as tong FROM resignation r
        join employee e on r.employee_id = e.id
        join jhi_user u on u.id = e.user_id
        WHERE u.login = :login and (EXTRACT(MONTH FROM submission_date) = :month AND EXTRACT(YEAR FROM submission_date) = :year)
        """,
        nativeQuery = true
    )
    long countByStatusAndSubmissionDate(@Param("month") int month, @Param("year") int year, @Param("login") String login);
}
