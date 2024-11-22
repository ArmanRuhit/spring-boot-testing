package com.example.unittesting.service;

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
    public void givenEmployee_whenSaveEmployee_thenReturnEmployee() {
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
    public void givenEmployee_whenSaveEmployee_thenThrowException() {
        //given - precondition
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .build();

        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going to test
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then verify the output
        Mockito.verify(employeeRepository, Mockito.never()).save(Mockito.any(Employee.class)) ;
    }
}
