package com.example.carrer_bridge.service;

import com.example.carrer_bridge.domain.entities.ExperienceDegree;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ExperienceDegreeService {
    ExperienceDegree save(ExperienceDegree experienceDegree);
    List<ExperienceDegree> findAll();
    Optional<ExperienceDegree> findById(Long id);
    ExperienceDegree update(ExperienceDegree experienceDegreeUpdated, Long id);
    void delete(Long id);

}
