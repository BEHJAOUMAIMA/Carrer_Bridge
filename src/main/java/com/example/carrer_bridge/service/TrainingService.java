package com.example.carrer_bridge.service;

import com.example.carrer_bridge.domain.entities.Training;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TrainingService {
    Training save(Training training);
    List<Training> findAll();
    Optional<Training> findById(Long id);
    Training update(Training trainingUpdated, Long id);
    void delete(Long id);
}
