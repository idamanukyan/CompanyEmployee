package com.example.companyemployeespring.controller;

import com.example.companyemployeespring.model.Company;
import com.example.companyemployeespring.service.CompanyService;
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
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/companies")
    public String companies(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        Page<Company> companyPage = companyService.findAll(
                PageRequest.of(page, size, Sort.by("id").ascending()));
        model.addAttribute("companies", companyPage);
        return "companies";
    }

    @GetMapping("/addCompany")
    public String addCompanyForm(Model model) {
        model.addAttribute("company", new Company());
        return "addCompany";
    }

    @PostMapping("/addCompany")
    public String addCompany(@Valid @ModelAttribute Company company,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "addCompany";
        }
        companyService.save(company);
        return "redirect:/companies";
    }

    @GetMapping("/editCompany/{id}")
    public String editCompanyForm(@PathVariable Long id, Model model) {
        Company company = companyService.findById(id);
        model.addAttribute("company", company);
        return "editCompany";
    }

    @PostMapping("/editCompany/{id}")
    public String editCompany(@PathVariable Long id,
                              @Valid @ModelAttribute Company company,
                              BindingResult result) {
        if (result.hasErrors()) {
            company.setId(id);
            return "editCompany";
        }
        companyService.update(id, company);
        return "redirect:/companies";
    }

    @PostMapping("/deleteCompany/{id}")
    public String deleteCompany(@PathVariable Long id) {
        companyService.deleteById(id);
        return "redirect:/companies";
    }
}
