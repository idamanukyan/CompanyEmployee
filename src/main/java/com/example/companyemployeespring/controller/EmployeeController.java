package com.example.companyemployeespring.controller;

import com.example.companyemployeespring.model.Employee;
import com.example.companyemployeespring.service.CompanyService;
import com.example.companyemployeespring.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final CompanyService companyService;

    @GetMapping("/employees")
    public String employees(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        Page<Employee> employeePage = employeeService.findAll(
                PageRequest.of(page, size, Sort.by("id").ascending()));
        model.addAttribute("employees", employeePage);
        return "employees";
    }

    @GetMapping("/addEmployee")
    public String addEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("companies", companyService.findAll());
        return "addEmployee";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(@Valid @ModelAttribute Employee employee,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("companies", companyService.findAll());
            return "addEmployee";
        }
        employeeService.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/editEmployee/{id}")
    public String editEmployeeForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.findById(id);
        model.addAttribute("employee", employee);
        model.addAttribute("companies", companyService.findAll());
        return "editEmployee";
    }

    @PostMapping("/editEmployee/{id}")
    public String editEmployee(@PathVariable Long id,
                               @Valid @ModelAttribute Employee employee,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            employee.setId(id);
            model.addAttribute("companies", companyService.findAll());
            return "editEmployee";
        }
        employeeService.update(id, employee);
        return "redirect:/employees";
    }

    @PostMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteById(id);
        return "redirect:/employees";
    }

    @GetMapping("/employees/{id}")
    public String singleEmployee(@PathVariable Long id, Model model) {
        Employee employee = employeeService.findById(id);
        model.addAttribute("employee", employee);
        return "singleEmployee";
    }
}
