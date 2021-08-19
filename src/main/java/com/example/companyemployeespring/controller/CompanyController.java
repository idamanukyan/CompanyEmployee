package com.example.companyemployeespring.controller;

import com.example.companyemployeespring.model.Company;
import com.example.companyemployeespring.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/companies")
    public String companies(ModelMap modelMap) {
        List<Company> all = companyRepository.findAll();
        modelMap.addAttribute("companies", all);
        return "companies";
    }

    @GetMapping("/addCompany")
    public String addCompany() {
        return "addCompany";
    }

    @PostMapping("/addCompany")
    public String addCompanyPost(@ModelAttribute Company company) {

        companyRepository.save(company);
        return "redirect:/companies";
    }

    @GetMapping("/deleteCompany")
    public String deleteCompany() {
        return "deleteCompany";
    }

    @PostMapping("/deleteCompany")
    public String deleteCompanyPost(@RequestParam("id") int id) {
        Company company = companyRepository.getById(id);
        companyRepository.delete(company);
        return "redirect:/companies";
    }

}
