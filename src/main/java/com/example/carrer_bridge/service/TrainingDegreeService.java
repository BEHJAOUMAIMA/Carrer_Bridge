package com.example.carrer_bridge.service;

import com.example.carrer_bridge.domain.entities.TrainingDegree;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TrainingDegreeService {
    TrainingDegree save(TrainingDegree trainingDegree);
    List<TrainingDegree> findAll();
    Optional<TrainingDegree> findById(Long id);
    TrainingDegree update(TrainingDegree trainingDegreeUpdated, Long id);
    void delete(Long id);
}
