package com.example.companyemployeespring.service;

import com.example.companyemployeespring.exception.ResourceNotFoundException;
import com.example.companyemployeespring.model.Company;
import com.example.companyemployeespring.model.Employee;
import com.example.companyemployeespring.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void findAll_ReturnsPageOfEmployees() {
        Company company = Company.builder().id(1L).name("Co").build();
        List<Employee> employees = Arrays.asList(
                Employee.builder().id(1L).name("John").surname("Doe").email("john@test.com")
                        .phoneNumber("123").salary("5000").position("Dev").company(company).build(),
                Employee.builder().id(2L).name("Jane").surname("Smith").email("jane@test.com")
                        .phoneNumber("456").salary("6000").position("QA").company(company).build()
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(employees, pageable, employees.size());
        when(employeeRepository.findAll(pageable)).thenReturn(page);

        Page<Employee> result = employeeService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
        verify(employeeRepository).findAll(pageable);
    }

    @Test
    void findById_ExistingId_ReturnsEmployee() {
        Employee employee = Employee.builder().id(1L).name("John").surname("Doe")
                .email("john@test.com").phoneNumber("123").salary("5000").position("Dev").build();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.findById(1L);

        assertEquals("John", result.getName());
    }

    @Test
    void findById_NonExistingId_ThrowsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.findById(99L));
    }

    @Test
    void save_ValidEmployee_ReturnsSaved() {
        Employee employee = Employee.builder().name("John").surname("Doe")
                .email("john@test.com").phoneNumber("123").salary("5000").position("Dev").build();
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeService.save(employee);

        assertEquals("John", result.getName());
        verify(employeeRepository).save(employee);
    }

    @Test
    void update_ExistingEmployee_UpdatesFields() {
        Company oldCompany = Company.builder().id(1L).name("Old Co").build();
        Company newCompany = Company.builder().id(2L).name("New Co").build();
        Employee existing = Employee.builder().id(1L).name("Old").surname("Name")
                .email("old@test.com").phoneNumber("111").salary("3000").position("Jr")
                .company(oldCompany).build();
        Employee updated = Employee.builder().name("New").surname("Surname")
                .email("new@test.com").phoneNumber("222").salary("5000").position("Sr")
                .company(newCompany).build();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(employeeRepository.save(existing)).thenReturn(existing);

        Employee result = employeeService.update(1L, updated);

        assertEquals("New", result.getName());
        assertEquals("new@test.com", result.getEmail());
        assertEquals("5000", result.getSalary());
        assertEquals(newCompany, result.getCompany());
    }

    @Test
    void deleteById_ExistingId_DeletesEmployee() {
        Employee employee = Employee.builder().id(1L).name("John").build();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteById(1L);

        verify(employeeRepository).delete(employee);
    }
}
