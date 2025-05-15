package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SalaryDistribute;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SalaryDistribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalaryDistributeRepository extends JpaRepository<SalaryDistribute, Long> {
    @Query(
        value = """
                    SELECT
            e.name AS employee_name,
            w.coefficients AS wage_coefficients,
            w.base_salary AS wage_base_salary,
            w.allowance AS wage_allowance,
            (
                SELECT COUNT(a.id)
                FROM attendance a
                WHERE a.employee_id = e.id\s
                AND a.date_ofwork BETWEEN sd.start_date AND sd.end_date
            ) AS total_work_days,
            (
                (w.base_salary * w.coefficients + w.allowance) *
                (
                    SELECT COUNT(a.id)
                    FROM attendance a
                    WHERE a.employee_id = e.id\s
                    AND a.date_ofwork BETWEEN sd.start_date AND sd.end_date
                )
            ) AS total_salary
        FROM
            employee e
        JOIN
            payroll p ON p.employee_id = e.id
        JOIN
            (
                SELECT c.employee_id, c.wage_id
                FROM contract c
                WHERE c.status = 'ACTIVE'
            ) c ON c.employee_id = e.id
        JOIN
            wage w ON c.wage_id = w.id
        JOIN
            salary_distribute sd ON sd.id = p.salary_distribute_id
        WHERE
            sd.id = :id
                       """,
        nativeQuery = true
    )
    List<Object[]> findDetails(@Param("id") String id);
}
