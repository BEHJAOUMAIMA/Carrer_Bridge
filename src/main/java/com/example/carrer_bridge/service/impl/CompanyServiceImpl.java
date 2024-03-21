package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Company;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.CompanyRepository;
import com.example.carrer_bridge.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public Company save(Company company) {
        Optional<Company> existingCompany= companyRepository.findByName(company.getName());
        if (existingCompany.isPresent()){
            throw new OperationException("Company already exists with name: " + company.getName());
        }
        return companyRepository.save(company);
    }

    @Override
    public List<Company> findAll() {
        List<Company> companies = companyRepository.findAll();
        if (companies.isEmpty()) {
            throw new OperationException("No companies found!");
        } else {
            return companies;
        }
    }

    @Override
    public Optional<Company> findById(Long id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isEmpty()) {
            throw new OperationException("No company found for this ID!");
        } else {
            return company;
        }
    }

    @Override
    public Company update(Company companyUpdated, Long id) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new OperationException("Company not found with id: " + id));
        existingCompany.setName(companyUpdated.getName());
        existingCompany.setDescription(companyUpdated.getDescription());
        existingCompany.setIndustry(companyUpdated.getIndustry());
        return companyRepository.save(existingCompany);    }

    @Override
    public void delete(Long id) {
        Company existingCompany= companyRepository.findById(id)
                .orElseThrow(() -> new OperationException("Company not found with id: " + id));
        companyRepository.delete(existingCompany);
    }
}
