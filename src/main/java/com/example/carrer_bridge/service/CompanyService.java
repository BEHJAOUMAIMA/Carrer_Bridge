package com.example.carrer_bridge.service;


import com.example.carrer_bridge.domain.entities.Company;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CompanyService {
    Company save(Company company);
    List<Company> findAll();
    Optional<Company> findById(Long id);
    Company update(Company companyUpdated, Long id);
    void delete(Long id);
}
