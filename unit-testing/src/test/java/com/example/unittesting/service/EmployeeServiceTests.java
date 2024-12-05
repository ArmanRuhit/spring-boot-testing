package com.example.unittesting.service;

import com.example.unittesting.exception.ResourceNotFoundException;
import com.example.unittesting.model.Employee;
import com.example.unittesting.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setup(){
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    //unit test for saveEmployee method
    @DisplayName("unit test for saveEmployee method")
    @Test
    void givenEmployee_whenSaveEmployee_thenReturnEmployee() {
        //given - precondition
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();

        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        System.out.println(savedEmployee);

        //then verify the output
        Assertions.assertThat(employee).isEqualTo(savedEmployee);

    }

    //unit test for saveEmployee method -> throws exception
    @DisplayName("unit test for saveEmployee method which throws exception")
    @Test
    void givenExistingEmployee_whenSaveEmployee_thenThrowException() {
        //given - precondition
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();

        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then verify the output
        Mockito.verify(employeeRepository, Mockito.never()).save(Mockito.any(Employee.class)) ;
    }

    // unit test for getAllEmployees method
    @DisplayName("unit test for getAllEmployees method")
    @Test
    void givenEmployeesList_whenGetEmployees_thenReturnEmployeesList() {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();
        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee1, employee2));

        //when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        Assertions.assertThat(employeeList)
                .isNotNull()
                .hasSize(2);
    }

    // unit test for unit test for getAllEmployees method (negative scenario)
    @DisplayName("unit test for unit test for getAllEmployees method (negative scenario)")
    @Test
    void givenEmptyEmployeesList_whenGetEmployees_thenReturnEmptyEmployeeList() {
        // given - precondition or setup

        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        Assertions.assertThat(employeeList)
                .isNotNull()
                .isEmpty();

    }

    // unit test for getEmployeeById method
    @DisplayName("unit test for getEmployeeById method")
    @Test
    void givenEmployee_whenGetEmployeeById_thenReturnEmployee() {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();

        BDDMockito.given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        //when - action or behaviour that we are going to test
        Optional<Employee> savedEmployee = employeeService.getEmployeeById(1L);
        //then - verify the output
        Assertions.assertThat(savedEmployee)
                .isPresent();
    }

    // unit test for updateEmployee method
    @DisplayName("unit test for updateEmployee method")
    @Test
    void givenEmployee_whenUpdateEmployee_thenUpdateEmployee() {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();

        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("ra@gmail.com");
        employee.setFirstName("hehe");
        //when - action or behaviour that we are going to test

        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then - verify the output
        Assertions.assertThat(updatedEmployee).isNotNull();
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("hehe");
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("ra@gmail.com");
    }

    // unit test for deleteEmployee method
    @DisplayName("unit test for deleteEmployee method")
    @Test
    void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        // given - precondition or setup
        BDDMockito.willDoNothing().given(employeeRepository).deleteById(1L);
        //when - action or behaviour that we are going to test
        employeeService.deleteEmployee(1L);
        //then - verify the output
        BDDMockito.verify(employeeRepository, Mockito.times(1)).deleteById(1L);
    }
}
