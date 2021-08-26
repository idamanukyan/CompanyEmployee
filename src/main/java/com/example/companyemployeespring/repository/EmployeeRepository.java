package com.example.companyemployeespring.repository;

import com.example.companyemployeespring.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> deleteAllByCompanyId(int id);
}




