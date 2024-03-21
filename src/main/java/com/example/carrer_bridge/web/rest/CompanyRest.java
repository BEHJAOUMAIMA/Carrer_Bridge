package com.example.carrer_bridge.web.rest;


import com.example.carrer_bridge.domain.entities.Company;
import com.example.carrer_bridge.dto.request.CompanyRequestDto;
import com.example.carrer_bridge.dto.response.CompanyResponseDto;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.CompanyMapper;
import com.example.carrer_bridge.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyRest {

    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    @GetMapping
    public ResponseEntity<List<CompanyResponseDto>> getAllCompanies() {
        List<Company> companies = companyService.findAll();
        List<CompanyResponseDto> CompanyResponseDtos = companies.stream().map(companyMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(CompanyResponseDtos);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long companyId) {
        Company Company = companyService.findById(companyId)
                .orElseThrow(() -> new OperationException("Company not found with ID: " + companyId));
        CompanyResponseDto CompanyResponseDto = companyMapper.toResponseDto(Company);
        return ResponseEntity.ok(CompanyResponseDto);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage> addCompany(@Valid @RequestBody CompanyRequestDto companyRequestDto) {
        Company Company = companyService.save(companyMapper.fromRequestDto(companyRequestDto));
        if (Company == null) {
            return ResponseMessage.badRequest("Company not created");
        } else {
            return ResponseMessage.created("Company created successfully", Company);
        }
    }

    @PutMapping("/update/{companyId}")
    public ResponseEntity<ResponseMessage> updateCompany(@PathVariable Long companyId, @Valid @RequestBody CompanyRequestDto companyRequestDto) {
        Company updatedCompany = companyMapper.fromRequestDto(companyRequestDto);
        Company Company = companyService.update(updatedCompany, companyId);
        return ResponseEntity.ok(ResponseMessage.created("Company updated successfully", Company).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteCompany(@PathVariable Long id) {
        Optional<Company> existingCompany = companyService.findById(id);
        if (existingCompany.isEmpty()) {
            return ResponseMessage.notFound("Company not found with ID: " + id);
        }
        companyService.delete(id);
        return ResponseMessage.ok("Company deleted successfully with ID: " + id, null);
    }
}
