package com.example.companyemployeespring.service;

import com.example.companyemployeespring.exception.ResourceNotFoundException;
import com.example.companyemployeespring.model.Company;
import com.example.companyemployeespring.repository.CompanyRepository;
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
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void findAll_ReturnsPageOfCompanies() {
        List<Company> companies = Arrays.asList(
                Company.builder().id(1L).name("Company A").size(10).address("Address A").build(),
                Company.builder().id(2L).name("Company B").size(20).address("Address B").build()
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<Company> page = new PageImpl<>(companies, pageable, companies.size());
        when(companyRepository.findAll(pageable)).thenReturn(page);

        Page<Company> result = companyService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
        verify(companyRepository).findAll(pageable);
    }

    @Test
    void findById_ExistingId_ReturnsCompany() {
        Company company = Company.builder().id(1L).name("Test").size(5).address("Addr").build();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Company result = companyService.findById(1L);

        assertEquals("Test", result.getName());
    }

    @Test
    void findById_NonExistingId_ThrowsException() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> companyService.findById(99L));
    }

    @Test
    void save_ValidCompany_ReturnsSavedCompany() {
        Company company = Company.builder().name("New Co").size(10).address("Addr").build();
        when(companyRepository.save(company)).thenReturn(company);

        Company result = companyService.save(company);

        assertEquals("New Co", result.getName());
        verify(companyRepository).save(company);
    }

    @Test
    void update_ExistingCompany_UpdatesFields() {
        Company existing = Company.builder().id(1L).name("Old").size(5).address("Old Addr").build();
        Company updated = Company.builder().name("New").size(10).address("New Addr").build();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(companyRepository.save(existing)).thenReturn(existing);

        Company result = companyService.update(1L, updated);

        assertEquals("New", result.getName());
        assertEquals(10, result.getSize());
        assertEquals("New Addr", result.getAddress());
    }

    @Test
    void deleteById_ExistingId_DeletesCompany() {
        Company company = Company.builder().id(1L).name("Test").build();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        companyService.deleteById(1L);

        verify(companyRepository).delete(company);
    }

    @Test
    void deleteById_NonExistingId_ThrowsException() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> companyService.deleteById(99L));
    }
}
