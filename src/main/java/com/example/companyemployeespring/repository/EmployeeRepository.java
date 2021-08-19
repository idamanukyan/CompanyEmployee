package com.example.companyemployeespring.repository;

import com.example.companyemployeespring.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}

