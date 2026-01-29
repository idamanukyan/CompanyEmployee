package com.example.companyemployeespring.service;

import com.example.companyemployeespring.exception.ResourceNotFoundException;
import com.example.companyemployeespring.model.Company;
import com.example.companyemployeespring.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Page<Company> findAll(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company findById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
    }

    @Transactional
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Transactional
    public Company update(Long id, Company updatedCompany) {
        Company existing = findById(id);
        existing.setName(updatedCompany.getName());
        existing.setSize(updatedCompany.getSize());
        existing.setAddress(updatedCompany.getAddress());
        return companyRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        Company company = findById(id);
        companyRepository.delete(company);
    }
}
