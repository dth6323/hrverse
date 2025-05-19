package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(
        value = """
                Select e.* from Employee e
                    join JHI_USER u on u.id = e.user_id
                    where u.email = :d
        """,
        nativeQuery = true
    )
    Optional<Employee> findByEmail(@Param("d") String email);

    @Query(
        value = """
        select e.ADDRESS,e.DATE_OF_BIRTH,d.DEPARTMENT_NAME,e.PHONE from Employee e
                            join department d on e.department_id = d.id
                            join JHI_USER j on e.USER_ID = j.ID
        where j.EMAIL = :d
        """,
        nativeQuery = true
    )
    List<Object[]> getInformation(@Param("d") String email);
}
