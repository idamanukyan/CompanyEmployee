package com.example.companyemployeespring.controller;

import com.example.companyemployeespring.model.Company;
import com.example.companyemployeespring.model.Employee;
import com.example.companyemployeespring.repository.CompanyRepository;
import com.example.companyemployeespring.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {


    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/employees")
    public String employees(ModelMap modelMap) {
        List<Employee> all = employeeRepository.findAll();
        modelMap.addAttribute("employees", all);
        return "employees";
    }

    @GetMapping("/addEmployee")
    public String addEmployee(ModelMap modelMap) {
        List<Company> all = companyRepository.findAll();
        modelMap.addAttribute("companies", all);
        return "addEmployee";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(@ModelAttribute Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/employees/{id}")
    public String singleEmployee(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (!employee.isPresent()) {
            return "redirect:/";
        }
        modelMap.addAttribute("employee", employee.get());
        return "singleEmployee";
    }
}
