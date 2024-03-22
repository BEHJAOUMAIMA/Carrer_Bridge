package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.TrainingDegree;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.TrainingDegreeRepository;
import com.example.carrer_bridge.service.TrainingDegreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrainingDegreeServiceImpl implements TrainingDegreeService {
    private final TrainingDegreeRepository trainingDegreeRepository;
    @Override
    public TrainingDegree save(TrainingDegree trainingDegree) {
        Optional<TrainingDegree> existingTrainingDegree= trainingDegreeRepository.findByDegree(trainingDegree.getDegree());
        if (existingTrainingDegree.isPresent()){
            throw new OperationException("Training Degree already exists with type: " + trainingDegree.getDegree());
        }
        return trainingDegreeRepository.save(trainingDegree);
    }

    @Override
    public List<TrainingDegree> findAll() {
        List<TrainingDegree> trainingDegrees = trainingDegreeRepository.findAll();
        if (trainingDegrees.isEmpty()) {
            throw new OperationException("No Training Degrees found!");
        } else {
            return trainingDegrees;
        }
    }

    @Override
    public Optional<TrainingDegree> findById(Long id) {
        Optional<TrainingDegree> trainingDegree = trainingDegreeRepository.findById(id);
        if (trainingDegree.isEmpty()) {
            throw new OperationException("No Training Degree found for this ID!");
        } else {
            return trainingDegree;
        }
    }

    @Override
    public TrainingDegree update(TrainingDegree trainingDegreeUpdated, Long id) {
        TrainingDegree existingTrainingDegree = trainingDegreeRepository.findById(id)
                .orElseThrow(() -> new OperationException("Training Degree not found with id: " + id));

        if (trainingDegreeRepository.existsByDegree(existingTrainingDegree.getDegree())) {
            throw new OperationException("Training Degree with this name already exists");
        }
        existingTrainingDegree.setDegree(trainingDegreeUpdated.getDegree());
        return trainingDegreeRepository.save(existingTrainingDegree);
    }

    @Override
    public void delete(Long id) {
        TrainingDegree existingTrainingDegree = trainingDegreeRepository.findById(id)
                .orElseThrow(() -> new OperationException("Training Degree not found with id: " + id));
        trainingDegreeRepository.delete(existingTrainingDegree);
    }
}
