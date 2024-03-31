package com.example.carrer_bridge.service;


import com.example.carrer_bridge.domain.entities.City;
import com.example.carrer_bridge.domain.entities.ExperienceDegree;
import com.example.carrer_bridge.domain.entities.JobOpportunity;
import com.example.carrer_bridge.domain.entities.TrainingDegree;
import com.example.carrer_bridge.domain.enums.ContractType;
import com.example.carrer_bridge.domain.enums.WorkingMode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface JobOpportunityService {
    JobOpportunity save(JobOpportunity jobOpportunity);
    List<JobOpportunity> findAll();
    List<JobOpportunity> getJobs();
    Optional<JobOpportunity> findById(Long id);
    JobOpportunity update(JobOpportunity jobOpportunityUpdated, Long id);
    void delete(Long id);
    public List<JobOpportunity> findByCriteria(String title, String description, String requiredSkills,
                                               LocalDateTime expirationDate, ContractType contractType,
                                               WorkingMode workingMode, City city,
                                               ExperienceDegree experienceDegree, TrainingDegree trainingDegree,
                                               LocalDateTime createdAt);
}
