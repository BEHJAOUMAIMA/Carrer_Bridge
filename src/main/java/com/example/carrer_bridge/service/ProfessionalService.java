package com.example.carrer_bridge.service;

import com.example.carrer_bridge.domain.entities.Professional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProfessionalService {
    Professional save(Professional professional);
    List<Professional> findAll();
    Optional<Professional> findById(Long id);
    Professional update(Professional professionalUpdated, Long id);
    void delete(Long id);
}
