package com.example.unittesting.repository;

import com.example.unittesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    // define custom query using JPQL with index parameters
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQLIndex(String firstName, String lastName);

    // define custom query using JPQL with name parameters
    @Query("select e from Employee e where e.firstName =:firstName and e.lastName =:lastName")
    Employee findByJPQLNamed(@Param("firstName") String firstName, @Param("lastName") String lastName);


    @Query(value = "select * from employee e where e.first_name = ?1 and e.last_name=?2", nativeQuery = true)
    Employee findByNativeSQLIndexedParam(String firstName, String lastName);

    @Query(value = "select * from employee e where e.first_name = :firstName and e.last_name= :lastName", nativeQuery = true)
    Employee findByNativeSQLNamedParam(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
