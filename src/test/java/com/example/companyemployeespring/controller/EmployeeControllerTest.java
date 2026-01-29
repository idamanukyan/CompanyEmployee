package com.example.companyemployeespring.controller;

import com.example.companyemployeespring.model.Company;
import com.example.companyemployeespring.model.Employee;
import com.example.companyemployeespring.service.CompanyService;
import com.example.companyemployeespring.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private CompanyService companyService;

    @Test
    @WithMockUser
    void employees_ReturnsEmployeesPage() throws Exception {
        Company company = Company.builder().id(1L).name("Co").build();
        List<Employee> employees = Arrays.asList(
                Employee.builder().id(1L).name("John").surname("Doe").email("j@t.com")
                        .phoneNumber("123").salary("5000").position("Dev").company(company).build()
        );
        when(employeeService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(employees));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees"))
                .andExpect(model().attributeExists("employees"));
    }

    @Test
    @WithMockUser
    void addEmployeeForm_ReturnsForm() throws Exception {
        when(companyService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/addEmployee"))
                .andExpect(status().isOk())
                .andExpect(view().name("addEmployee"))
                .andExpect(model().attributeExists("employee", "companies"));
    }

    @Test
    @WithMockUser
    void addEmployee_ValidData_Redirects() throws Exception {
        mockMvc.perform(post("/addEmployee").with(csrf())
                        .param("name", "John")
                        .param("surname", "Doe")
                        .param("email", "john@test.com")
                        .param("phoneNumber", "123456")
                        .param("salary", "5000")
                        .param("position", "Developer")
                        .param("company.id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"));
    }

    @Test
    @WithMockUser
    void addEmployee_InvalidData_ReturnsForm() throws Exception {
        when(companyService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/addEmployee").with(csrf())
                        .param("name", "")
                        .param("surname", "")
                        .param("email", "invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("addEmployee"));
    }

    @Test
    @WithMockUser
    void singleEmployee_ReturnsDetails() throws Exception {
        Company company = Company.builder().id(1L).name("Co").size(10).address("Addr").build();
        Employee employee = Employee.builder().id(1L).name("John").surname("Doe")
                .email("j@t.com").phoneNumber("123").salary("5000").position("Dev").company(company).build();
        when(employeeService.findById(1L)).thenReturn(employee);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("singleEmployee"))
                .andExpect(model().attributeExists("employee"));
    }

    @Test
    @WithMockUser
    void deleteEmployee_PostMethod_Redirects() throws Exception {
        mockMvc.perform(post("/deleteEmployee/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"));
    }
}
