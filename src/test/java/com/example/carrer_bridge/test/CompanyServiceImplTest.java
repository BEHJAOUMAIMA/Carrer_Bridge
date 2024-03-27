package com.example.carrer_bridge.test;

import com.example.carrer_bridge.domain.entities.Company;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.CompanyRepository;
import com.example.carrer_bridge.service.impl.CompanyServiceImpl;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CompanyServiceImplTest {

    @Test
    public void test_save_new_company_successfully() {
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
    
        Company company = new Company();
        company.setName("Test Company");
        company.setDescription("Test Description");
        company.setIndustry("Test Industry");

        when(companyRepository.findByName(company.getName())).thenReturn(Optional.empty());
        when(companyRepository.save(company)).thenReturn(company);
    
        Company savedCompany = companyService.save(company);
    
        assertEquals(company, savedCompany);
    }

    @Test
    public void test_retrieve_all_companies_successfully() {
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
    
        List<Company> companies = new ArrayList<>();
        companies.add(new Company(1L, "Company 1", "Description 1", "Industry 1"));
        companies.add(new Company(2L, "Company 2", "Description 2", "Industry 2"));
    
        when(companyRepository.findAll()).thenReturn(companies);
    
        List<Company> retrievedCompanies = companyService.findAll();
    
        assertEquals(companies, retrievedCompanies);
    }

    @Test
    public void test_retrieve_company_by_id_successfully() {
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
    
        Long companyId = 1L;
        Company company = new Company(companyId, "Test Company", "Test Description", "Test Industry");
    
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
    
        Optional<Company> retrievedCompany = companyService.findById(companyId);
    
        assertEquals(Optional.of(company), retrievedCompany);
    }

    @Test
    public void test_update_company_successfully() {
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
    
        Long companyId = 1L;
        Company existingCompany = new Company(companyId, "Existing Company", "Existing Description", "Existing Industry");
        Company updatedCompany = new Company(companyId, "Updated Company", "Updated Description", "Updated Industry");
    
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(existingCompany)).thenReturn(updatedCompany);
    
        Company savedCompany = companyService.update(updatedCompany, companyId);
    
        assertEquals(updatedCompany, savedCompany);
    }

    @Test
    public void test_delete_company_successfully() {
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
    
        Long companyId = 1L;
        Company existingCompany = new Company(companyId, "Existing Company", "Existing Description", "Existing Industry");
    
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
    
        companyService.delete(companyId);
    
        verify(companyRepository).delete(existingCompany);
    }

    @Test
    public void test_save_company_with_existing_name_throws_operation_exception() {
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
    
        Company company = new Company();
        company.setName("Existing Company");
        company.setDescription("Test Description");
        company.setIndustry("Test Industry");
    
        when(companyRepository.findByName(company.getName())).thenReturn(Optional.of(company));
    
        assertThrows(OperationException.class, () -> companyService.save(company));
    }

    @Test
    public void test_retrieve_all_companies_when_no_companies_exist_throws_operation_exception() {
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
    
        when(companyRepository.findAll()).thenReturn(Collections.emptyList());
    
        assertThrows(OperationException.class, companyService::findAll);
    }

    @Test
    public void test_retrieve_company_by_invalid_id_throws_operation_exception() {
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
    
        Long invalidId = 999L;
    
        when(companyRepository.findById(invalidId)).thenReturn(Optional.empty());
    
        assertThrows(OperationException.class, () -> companyService.findById(invalidId));
    }

    @Test
    public void test_update_company_with_invalid_id_throws_operation_exception() {
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
    
        Long invalidId = 999L;
        Company updatedCompany = new Company(invalidId, "Updated Company", "Updated Description", "Updated Industry");
    
        when(companyRepository.findById(invalidId)).thenReturn(Optional.empty());
    
        assertThrows(OperationException.class, () -> companyService.update(updatedCompany, invalidId));
    }

    @Test
    public void test_delete_company_with_invalid_id_throws_operation_exception() {
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
    
        Long invalidId = 999L;
    
        when(companyRepository.findById(invalidId)).thenReturn(Optional.empty());
    
        assertThrows(OperationException.class, () -> companyService.delete(invalidId));
    }

    @Test
    public void test_save_company_with_null_name_throws_operation_exception() {
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
    
        Company company = new Company();
        company.setName(null);
        company.setDescription("Test Description");
        company.setIndustry("Test Industry");
    
        assertThrows(OperationException.class, () -> companyService.save(company));
    }

    @Test
    public void test_save_company_with_null_description_throws_operation_exception() {
        CompanyRepository companyRepository = mock(CompanyRepository.class);
        CompanyServiceImpl companyService = new CompanyServiceImpl(companyRepository);
    
        Company company = new Company();
        company.setName("Test Company");
        company.setDescription(null);
        company.setIndustry("Test Industry");
    
        assertThrows(OperationException.class, () -> companyService.save(company));
    }

}