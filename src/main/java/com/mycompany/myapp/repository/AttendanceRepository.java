package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Attendance;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Attendance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByDateOfworkBetween(LocalDate startDate, LocalDate endDate);
}
