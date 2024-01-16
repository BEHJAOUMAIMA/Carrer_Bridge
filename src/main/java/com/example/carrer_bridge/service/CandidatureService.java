package com.example.carrer_bridge.service;


import com.example.carrer_bridge.domain.entities.Candidature;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CandidatureService {
    Candidature save(Candidature candidature);
    List<Candidature> findAll();
    Optional<Candidature> findById(Long id);
    Candidature update(Candidature candidatureUpdated, Long id);
    void delete(Long id);

}
