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
               SELECT\s
            e.name AS employee_name,
            w.coefficients AS wage_coefficients,
            w.base_salary AS wage_base_salary,
            w.allowance AS wage_allowance,
            (
                SELECT COUNT(a.id)
                FROM attendance a
                WHERE a.employee_id = e.id AND a.date_ofwork BETWEEN sd.start_Date AND sd.end_Date
            ) AS total_work_days,
            (
                (w.base_salary * w.coefficients + w.allowance) *
                (
                    SELECT COUNT(a.id)
                    FROM attendance a
                    WHERE a.employee_id = e.id AND a.date_ofwork BETWEEN sd.start_Date AND sd.end_Date
                )
            ) AS total_salary
        FROM\s
            employee e
        JOIN\s
            payroll p ON p.employee_id = e.id
        JOIN\s
            wage w ON p.wage_id = w.id
        Join salary_distribute sd On sd.id = p.salary_distribute_id
        where sd.id = :id
               """,
        nativeQuery = true
    )
    List<Object[]> findDetails(@Param("id") String id);
}
