package com.example.carrer_bridge.service;


import com.example.carrer_bridge.domain.entities.JobOpportunity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface JobOpportunityService {
    JobOpportunity save(JobOpportunity jobOpportunity);
    List<JobOpportunity> findAll();
    Optional<JobOpportunity> findById(Long id);
    JobOpportunity update(JobOpportunity jobOpportunityUpdated, Long id);
    void delete(Long id);
}
