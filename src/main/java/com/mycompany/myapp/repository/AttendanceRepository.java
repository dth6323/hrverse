package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Attendance;
import com.mycompany.myapp.domain.Resignation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Attendance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByDateOfworkBetween(LocalDate startDate, LocalDate endDate);

    @Query(
        value = """
            SELECT a.*
            FROM Attendance a
            JOIN employee e ON a.employee_id = e.id
            JOIN jhi_user u ON u.id = e.user_id
            WHERE u.login = :login
        """,
        countQuery = """
            SELECT COUNT(*)
            FROM Attendance a
            JOIN employee e ON a.employee_id = e.id
            JOIN jhi_user u ON u.id = e.user_id
            WHERE u.login = :login
        """,
        nativeQuery = true
    )
    Page<Attendance> getAttend(@Param("login") String login, Pageable pageable);
}
