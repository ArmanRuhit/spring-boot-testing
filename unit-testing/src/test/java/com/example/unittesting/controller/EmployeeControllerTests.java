package com.example.unittesting.controller;

import com.example.unittesting.model.Employee;
import com.example.unittesting.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    //unit test for create employee controller
    @DisplayName("unit test for create employee controller")
    @Test
    public void givenEmployee_whenCreateEmployee_thenReturnSavedEmployee(){
        //given - precondition
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        // valid for any employee object
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class))).willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));

        //when - action or the behaviour that we are going to test
        try {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(employee))
            );

            //then verify the output
            response.andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(employee.getFirstName()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(employee.getLastName()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employee.getEmail()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //unit test for getAllEmployees rest api
    @DisplayName("unit test for getAllEmployees rest api")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() {
        //given - precondition
        Employee employee1 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        List<Employee> employees = Arrays.asList(employee1, employee2);
        BDDMockito.given(employeeService.getAllEmployees()).willReturn(employees);

        //when - action or the behaviour that we are going to test
        try {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

            //then verify the output
            response.andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //unit test for getEmployeeById rest api (positive scenario)
    @DisplayName("unit test for getEmployeeById rest api (positive scenario)")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() {
        //given - precondition
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going to test
        try {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

            //then verify the output
            response.andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(employee.getFirstName()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //unit test for getEmployeeById rest api (negative scenario)
    @DisplayName("unit test for getEmployeeById rest api (negative scenario)")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() {
        //given - precondition
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or the behaviour that we are going to test
        try {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

            //then verify the output
            response.andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //unit test for updateEmployee rest api (positive scenario)
    @DisplayName("unit test for updateEmployee rest api (positive scenario)")
    @Test
    public void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given - precondition
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        Employee updatedEmployee = Employee.builder()
                .id(employeeId)
                .firstName("John1")
                .lastName("Doe1")
                .email("john1.doe1@example.com")
                .build();

        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class))).willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));


        //when - action or the behaviour that we are going to test
        try {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
                            .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedEmployee)));

            //then verify the output
            response.andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(updatedEmployee.getFirstName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(updatedEmployee.getLastName()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(updatedEmployee.getEmail()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //unit test for updateEmployee rest api (negative scenario)
    @DisplayName("unit test for updateEmployee rest api (negative scenario)")
    @Test
    public void givenEmployee_whenUpdateEmployee_thenReturnEmptyEmployee() {
        //given - precondition
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        Employee updatedEmployee = Employee.builder()
                .id(employeeId)
                .firstName("John1")
                .lastName("Doe1")
                .email("john1.doe1@example.com")
                .build();

        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class))).willAnswer((invocationOnMock -> invocationOnMock.getArgument(0)));


        //when - action or the behaviour that we are going to test
        try {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedEmployee)));

            //then verify the output
            response.andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //unit test for delete employee rest api
    @DisplayName("unit test for delete employee rest api")
    @Test
    public void given_when_then() {
        //given - precondition
        BDDMockito.willDoNothing().given(employeeService).deleteEmployee(ArgumentMatchers.anyLong());
        //when - action or the behaviour that we are going to test
        try {
            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", 1L));

            //then verify the output
            response.andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
