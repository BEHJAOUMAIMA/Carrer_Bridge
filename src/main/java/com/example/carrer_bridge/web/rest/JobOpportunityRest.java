package com.example.carrer_bridge.web.rest;

import com.example.carrer_bridge.domain.entities.*;
import com.example.carrer_bridge.domain.enums.ContractType;
import com.example.carrer_bridge.domain.enums.WorkingMode;
import com.example.carrer_bridge.dto.request.JobOpportunityRequestDto;
import com.example.carrer_bridge.dto.response.JobOpportunityResponseDto;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.JobOpportunityMapper;
import com.example.carrer_bridge.service.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
public class JobOpportunityRest {

    private final JobOpportunityService jobOpportunityService;
    private final JobOpportunityMapper jobOpportunityMapper;
    private final CompanyService companyService;
    private final CityService cityService;
    private final TrainingDegreeService trainingDegreeService;
    private final ExperienceDegreeService experienceDegreeService;

    @GetMapping
    public ResponseEntity<List<JobOpportunityResponseDto>> getAllJobOpportunities() {
        List<JobOpportunity> jobOpportunities = jobOpportunityService.findAll();
        List<JobOpportunityResponseDto> jobOpportunityResponseDtos = jobOpportunities.stream().map(jobOpportunityMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(jobOpportunityResponseDtos);
    }

    @GetMapping("/{jobOpportunityId}")
    public ResponseEntity<?> getJobOpportunityById(@PathVariable Long jobOpportunityId) {
        JobOpportunity jobOpportunity = jobOpportunityService.findById(jobOpportunityId)
                .orElseThrow(() -> new OperationException("Job Opportunity not found with ID: " + jobOpportunityId));
        JobOpportunityResponseDto jobOpportunityResponseDto = jobOpportunityMapper.toResponseDto(jobOpportunity);
        return ResponseEntity.ok(jobOpportunityResponseDto);
    }

    @PostMapping("/save")
    @Transactional
    public ResponseEntity<ResponseMessage> addJobOpportunity(@Valid @RequestBody JobOpportunityRequestDto jobOpportunityRequestDto) {
        Optional<Company> companyOptional = companyService.findById(jobOpportunityRequestDto.getCompanyId());
        Optional<City> cityOptional = cityService.findById(jobOpportunityRequestDto.getCityId());
        Optional<ExperienceDegree> experienceDegreeOptional = experienceDegreeService.findById(jobOpportunityRequestDto.getExperienceDegreeId());
        Optional<TrainingDegree> trainingDegreeOptional = trainingDegreeService.findById(jobOpportunityRequestDto.getTrainingDegreeId());

        if (companyOptional.isEmpty() || cityOptional.isEmpty() || experienceDegreeOptional.isEmpty() || trainingDegreeOptional.isEmpty()) {
            return ResponseMessage.badRequest("Failed to retrieve associated entities");
        }

        Company company = companyOptional.get();
        City city = cityOptional.get();
        ExperienceDegree experienceDegree = experienceDegreeOptional.get();
        TrainingDegree trainingDegree = trainingDegreeOptional.get();

        JobOpportunity jobOpportunity = jobOpportunityMapper.fromRequestDto(jobOpportunityRequestDto);
        jobOpportunity.setCompany(company);
        jobOpportunity.setCity(city);
        jobOpportunity.setExperienceDegree(experienceDegree);
        jobOpportunity.setTrainingDegree(trainingDegree);

        JobOpportunity savedJobOpportunity = jobOpportunityService.save(jobOpportunity);

        if (savedJobOpportunity == null) {
            return ResponseMessage.badRequest("Job Opportunity not created");
        } else {
            return ResponseMessage.created("Job Opportunity created successfully", savedJobOpportunity);
        }
    }


    @PutMapping("/update/{jobOpportunityId}")
    public ResponseEntity<ResponseMessage> updateJobOpportunity(@PathVariable Long jobOpportunityId, @Valid @RequestBody JobOpportunityRequestDto jobOpportunityRequestDto) {
        JobOpportunity updatedJobOpportunity = jobOpportunityMapper.fromRequestDto(jobOpportunityRequestDto);
        JobOpportunity jobOpportunity = jobOpportunityService.update(updatedJobOpportunity, jobOpportunityId);
        return ResponseEntity.ok(ResponseMessage.created("Job Opportunity updated successfully", jobOpportunity).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteJobOpportunity(@PathVariable Long id) {
        Optional<JobOpportunity> existingJobOpportunity = jobOpportunityService.findById(id);
        if (existingJobOpportunity.isEmpty()) {
            return ResponseMessage.notFound("Job Opportunity not found with ID: " + id);
        }
        jobOpportunityService.delete(id);
        return ResponseMessage.ok("Job Opportunity deleted successfully with ID: " + id, null);
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobOpportunityResponseDto>> searchJobOpportunities(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String requiredSkills,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expirationDate,
            @RequestParam(required = false) ContractType contractType,
            @RequestParam(required = false) WorkingMode workingMode,
            @RequestParam(required = false) City city,
            @RequestParam(required = false) ExperienceDegree experienceDegree,
            @RequestParam(required = false) TrainingDegree trainingDegree,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt) {

        List<JobOpportunity> jobOpportunities = jobOpportunityService.findByCriteria(
                title, description, requiredSkills, expirationDate, contractType,
                workingMode, city, experienceDegree, trainingDegree, createdAt);

        List<JobOpportunityResponseDto> jobOpportunityResponseDtos = jobOpportunities.stream()
                .map(jobOpportunityMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(jobOpportunityResponseDtos);
    }
}
