package com.example.companyemployeespring.controller;

import com.example.companyemployeespring.model.Company;
import com.example.companyemployeespring.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;

    @Test
    @WithMockUser
    void companies_ReturnsCompaniesPage() throws Exception {
        List<Company> companies = Arrays.asList(
                Company.builder().id(1L).name("Co A").size(10).address("Addr A").build()
        );
        when(companyService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(companies));

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(view().name("companies"))
                .andExpect(model().attributeExists("companies"));
    }

    @Test
    @WithMockUser
    void addCompanyForm_ReturnsForm() throws Exception {
        mockMvc.perform(get("/addCompany"))
                .andExpect(status().isOk())
                .andExpect(view().name("addCompany"))
                .andExpect(model().attributeExists("company"));
    }

    @Test
    @WithMockUser
    void addCompany_ValidData_RedirectsToCompanies() throws Exception {
        mockMvc.perform(post("/addCompany").with(csrf())
                        .param("name", "Test Co")
                        .param("size", "10")
                        .param("address", "123 Street"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/companies"));
    }

    @Test
    @WithMockUser
    void addCompany_InvalidData_ReturnsForm() throws Exception {
        mockMvc.perform(post("/addCompany").with(csrf())
                        .param("name", "")
                        .param("size", "0")
                        .param("address", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("addCompany"));
    }

    @Test
    @WithMockUser
    void deleteCompany_PostMethod_Redirects() throws Exception {
        mockMvc.perform(post("/deleteCompany/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/companies"));
    }

    @Test
    void companies_Unauthenticated_IsUnauthorized() throws Exception {
        mockMvc.perform(get("/companies"))
                .andExpect(status().isUnauthorized());
    }
}
