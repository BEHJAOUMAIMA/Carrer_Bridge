package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.JobOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobOpportunityRepository extends JpaRepository<JobOpportunity, Long> {
}
