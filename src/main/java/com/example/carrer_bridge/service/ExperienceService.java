package com.example.carrer_bridge.service;

import com.example.carrer_bridge.domain.entities.Experience;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ExperienceService {
    Experience save(Experience experience);
    List<Experience> findAll();
    Optional<Experience> findById(Long id);
    Experience update(Experience experienceUpdated, Long id);
    void delete(Long id);
    List<Experience> findExperienceByTitle(String title);
    List<Experience> findExperiencesByUser();
}
