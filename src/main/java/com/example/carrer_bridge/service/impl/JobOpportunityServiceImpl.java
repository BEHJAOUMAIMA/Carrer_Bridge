package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.*;
import com.example.carrer_bridge.domain.enums.ContractType;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.domain.enums.WorkingMode;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.JobOpportunityRepository;
import com.example.carrer_bridge.service.JobOpportunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JobOpportunityServiceImpl implements JobOpportunityService {

    private final JobOpportunityRepository jobOpportunityRepository;

    @Override
    public JobOpportunity save(JobOpportunity jobOpportunity) {

        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!authenticatedUser.getRole().getRoleType().equals(RoleType.RECRUITER) &&
            !authenticatedUser.getRole().getRoleType().equals(RoleType.ADMINISTRATOR)){
            throw new OperationException("User is not authorized to create a job opportunity");
        }
        if (jobOpportunityRepository.existsByTitleAndUser_IdAndCompany_Id(jobOpportunity.getTitle(),
                authenticatedUser.getId(),jobOpportunity.getCompany().getId())){
            throw new OperationException("Job opportunity with the same title already exists for this recruiter and company");
        }
        jobOpportunity.setUser(authenticatedUser);
        jobOpportunity.setCreatedAt(LocalDateTime.now());
        return jobOpportunityRepository.save(jobOpportunity);
    }

    @Override
    public List<JobOpportunity> findAll() {
        List<JobOpportunity> jobOpportunities = jobOpportunityRepository.findAll();
        if (jobOpportunities.isEmpty()) {
            throw new OperationException("No job opportunities found !");
        } else {
            return jobOpportunities;
        }
    }

    @Override
    public Optional<JobOpportunity> findById(Long id) {
        Optional<JobOpportunity> jobOpportunity = jobOpportunityRepository.findById(id);
        if (jobOpportunity.isEmpty()) {
            throw new OperationException("No job opportunity found for this ID!");
        } else {
            return jobOpportunity;
        }
    }

    @Override
    public JobOpportunity update(JobOpportunity jobOpportunityUpdated, Long id) {
        JobOpportunity existingJobOpportunity = jobOpportunityRepository.findById(id)
                .orElseThrow(()->new OperationException("Job opportunity not found with ID:" + id));
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!authenticatedUser.getRole().getRoleType().equals(RoleType.RECRUITER) &&
                !authenticatedUser.getRole().getRoleType().equals(RoleType.ADMINISTRATOR)){
            throw new OperationException("User is not authorized to update a job opportunity");
        }
        if (!existingJobOpportunity.getUser().getId().equals(authenticatedUser.getId())){
            throw new OperationException("User is not authorized to update this job opportunity");
        }
        if (!existingJobOpportunity.getTitle().equals(jobOpportunityUpdated.getTitle())){
            if (jobOpportunityRepository.existsByTitleAndUser_IdAndCompany_Id(jobOpportunityUpdated.getTitle(),
                    authenticatedUser.getId(),jobOpportunityUpdated.getCompany().getId())){
                throw new OperationException("Another job opportunity with the same title already exists for this recruiter and company");
            }
        }
        existingJobOpportunity.setTitle(jobOpportunityUpdated.getTitle());
        existingJobOpportunity.setDescription(jobOpportunityUpdated.getDescription());
        existingJobOpportunity.setRequiredSkills(jobOpportunityUpdated.getRequiredSkills());
        existingJobOpportunity.setExpirationDate(jobOpportunityUpdated.getExpirationDate());
        existingJobOpportunity.setContractType(jobOpportunityUpdated.getContractType());
        existingJobOpportunity.setWorkingMode(jobOpportunityUpdated.getWorkingMode());
        existingJobOpportunity.setCity(jobOpportunityUpdated.getCity());
        existingJobOpportunity.setExperienceDegree(jobOpportunityUpdated.getExperienceDegree());
        existingJobOpportunity.setTrainingDegree(jobOpportunityUpdated.getTrainingDegree());
        existingJobOpportunity.setUpdatedAt(LocalDateTime.now());
        return jobOpportunityRepository.save(existingJobOpportunity);
    }

    @Override
    public void delete(Long id) {
        JobOpportunity existingJobOpportunity = jobOpportunityRepository.findById(id)
                .orElseThrow(() -> new OperationException("Job Opportunity not found with id: " + id));
        jobOpportunityRepository.delete(existingJobOpportunity);
    }

    @Override
    public List<JobOpportunity> findByCriteria(String title, String description,
                                               String requiredSkills, LocalDateTime expirationDate,
                                               ContractType contractType, WorkingMode workingMode,
                                               City city, ExperienceDegree experienceDegree,
                                               TrainingDegree trainingDegree, LocalDateTime createdAt) {

        JobOpportunity example = JobOpportunity.builder()
                .title(title)
                .description(description)
                .requiredSkills(requiredSkills)
                .expirationDate(expirationDate)
                .contractType(contractType)
                .workingMode(workingMode)
                .city(city)
                .experienceDegree(experienceDegree)
                .trainingDegree(trainingDegree)
                .createdAt(createdAt)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<JobOpportunity> jobOpportunityExample = Example.of(example, matcher);
        return jobOpportunityRepository.findAll(jobOpportunityExample);
    }
}
