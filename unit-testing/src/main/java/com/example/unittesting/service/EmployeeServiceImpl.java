package com.example.unittesting.service;

import com.example.unittesting.exception.ResourceNotFoundException;
import com.example.unittesting.model.Employee;
import com.example.unittesting.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> findOptionalExistingEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (findOptionalExistingEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee with email " + employee.getEmail() + " already exists");
        }

        return employeeRepository.save(employee);
    }
}
