package com.example.carrer_bridge.service;

import com.example.carrer_bridge.domain.entities.Recruiter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RecruiterService {
    Recruiter save(Recruiter recruiter);
    List<Recruiter> findAll();
    Optional<Recruiter> findById(Long id);
    Recruiter update(Recruiter recruiterUpdated, Long id);
    void delete(Long id);
}
