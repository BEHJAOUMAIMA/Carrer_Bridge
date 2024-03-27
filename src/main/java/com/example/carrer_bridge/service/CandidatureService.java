package com.example.carrer_bridge.service;


import com.example.carrer_bridge.domain.entities.Candidature;
import com.example.carrer_bridge.domain.enums.CandidatureStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CandidatureService {
    List<Candidature> findAll() ;
    Optional<Candidature> findById(Long id) ;
    List<Candidature> findByUserId(Long userId);
    List<Candidature> findByJobOpportunityId(Long jobOpportunityId);
    List<Candidature> findByStatus(CandidatureStatus status);
    Candidature save(Candidature candidature);
    Candidature update(Candidature candidatureUpdated, Long id);
    void delete(Long id);
    void acceptCandidature(Long candidatureId);
    void refuseCandidature(Long candidatureId);
}
