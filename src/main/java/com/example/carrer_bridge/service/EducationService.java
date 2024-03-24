package com.example.carrer_bridge.service;


import com.example.carrer_bridge.domain.entities.Education;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EducationService {

    Education save(Education education);
    List<Education> findAll();
    Optional<Education> findById(Long id);
    Education update(Education educationUpdated, Long id);
    void delete(Long id);
    List<Education> findEducationByDegree(String degree);
    List<Education> findEducationsByUser();

}
