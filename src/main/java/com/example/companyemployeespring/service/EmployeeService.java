package com.example.companyemployeespring.service;

import com.example.companyemployeespring.exception.ResourceNotFoundException;
import com.example.companyemployeespring.model.Employee;
import com.example.companyemployeespring.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    @Transactional
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee update(Long id, Employee updatedEmployee) {
        Employee existing = findById(id);
        existing.setName(updatedEmployee.getName());
        existing.setSurname(updatedEmployee.getSurname());
        existing.setEmail(updatedEmployee.getEmail());
        existing.setPhoneNumber(updatedEmployee.getPhoneNumber());
        existing.setSalary(updatedEmployee.getSalary());
        existing.setPosition(updatedEmployee.getPosition());
        existing.setCompany(updatedEmployee.getCompany());
        return employeeRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        Employee employee = findById(id);
        employeeRepository.delete(employee);
    }
}
