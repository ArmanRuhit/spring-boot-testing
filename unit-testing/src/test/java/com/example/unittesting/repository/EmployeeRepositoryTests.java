package com.example.unittesting.repository;

import com.example.unittesting.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    // unit test for save employee operation
    @DisplayName("unit test for save employee operation")
    @Test
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        //given
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();

        //when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getId()).isPositive();
    }

    // unit test for getting all employees
    @DisplayName("unit test for getting all employees")
    @Test
    void givenEmployeesList_whenFindAll_thenEmployeesList() {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Suresh")
                .lastName("Suresh")
                .email("suresh@gmail.com")
                .build();

        employeeRepository.saveAll(List.of(employee1, employee2));

        //when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        Assertions.assertThat(employeeList)
                .isNotNull()
                .hasSize(2);
    }

    // unit test for get employee by id operation
    @DisplayName("unit test for get employee by id operation")
    @Test
    void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();

        employeeRepository.save(employee);

        //when - action or behaviour that we are going to test
        Optional<Employee> employeeDBOptional = employeeRepository.findById(employee.getId());

        //then - verify the output
        Assertions.assertThat(employeeDBOptional)
                .isPresent()
                .isNotNull();
    }


    // unit test for get employee by email operation
    @DisplayName("unit test for get employee by email operation")
    @Test
    void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeObject() {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or behaviour that we are going to test
        Optional<Employee> employeeDBOptional = employeeRepository.findByEmail(employee.getEmail());

        //then - verify the output
        Assertions.assertThat(employeeDBOptional)
                .isPresent()
                .isNotNull();

    }

    // unit test for update employee operation
    @DisplayName("unit test for update employee operation")
    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or behaviour that we are going to test
        employee.setEmail("updated@gmail.com");
        employeeRepository.save(employee);

        Optional<Employee> employeeDBOptional = employeeRepository.findById(employee.getId());
        //then - verify the output
        Assertions.assertThat(employeeDBOptional)
                .isPresent()
                .isNotNull();

        Assertions.assertThat(employeeDBOptional.get().getEmail()).isEqualTo("updated@gmail.com");

    }

    // unit test for delete employee operation
    @DisplayName("unit test for delete employee operation")
    @Test
    void givenEmployee_whenDelete_thenRemoveEmployee() {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or behaviour that we are going to test
        employeeRepository.delete(employee);

        //then - verify the output
        Optional<Employee> employeeDBOptional = employeeRepository.findById(employee.getId());

        Assertions.assertThat(employeeDBOptional)
                .isEmpty();

    }

    // unit test for custom query using JPQL with index
    @DisplayName("unit test for custom query using JPQL with index")
    @Test
    void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployee() {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or behaviour that we are going to test
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        Employee employeeDB = employeeRepository.findByJPQLIndex(firstName, lastName);

        //then - verify the output
        Assertions.assertThat(employeeDB).isNotNull();
        Assertions.assertThat(employeeDB.getFirstName()).isEqualTo(firstName);
        Assertions.assertThat(employeeDB.getLastName()).isEqualTo(lastName);

    }


    // unit test for custom query using JPQL with named
    @DisplayName("unit test for custom query using JPQL with named parameter")
    @Test
    void givenFirstNameAndLastName_whenFindByJPQLNamed_thenReturnEmployee() {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or behaviour that we are going to test
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        Employee employeeDB = employeeRepository.findByJPQLNamed(firstName, lastName);

        //then - verify the output
        Assertions.assertThat(employeeDB).isNotNull();
        Assertions.assertThat(employeeDB.getFirstName()).isEqualTo(firstName);
        Assertions.assertThat(employeeDB.getLastName()).isEqualTo(lastName);

    }

    // unit test for custom query using native query with index
    @DisplayName("unit test for custom query using native query with index")
    @Test
    void givenFirstNameAndLastName_whenFindByNativeQueryIndex_thenReturnEmployee() {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Ramesh")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or behaviour that we are going to test
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        Employee employeeDB = employeeRepository.findByNativeSQL(firstName, lastName);

        //then - verify the output
        Assertions.assertThat(employeeDB).isNotNull();
        Assertions.assertThat(employeeDB.getFirstName()).isEqualTo(firstName);
        Assertions.assertThat(employeeDB.getLastName()).isEqualTo(lastName);

    }
}
