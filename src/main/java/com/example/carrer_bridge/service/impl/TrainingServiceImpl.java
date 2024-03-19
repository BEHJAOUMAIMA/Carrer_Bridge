package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.entities.Training;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.TrainingRepository;
import com.example.carrer_bridge.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;

    @Override
    public Training save(Training training) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (training == null) {
            throw new OperationException("Training object cannot be null");
        }
        List<Training> existingTrainings = trainingRepository.findByTitleAndUser(training.getTitle(), authenticatedUser);
        if (!existingTrainings.isEmpty()) {
            throw new OperationException("A training with the same title already exists for this user");
        }
        training.setUser(authenticatedUser);
        return trainingRepository.save(training);
    }

    @Override
    public List<Training> findAll() {
        List<Training> trainings = trainingRepository.findAll();
        if (trainings.isEmpty()) {
            throw new OperationException("No Trainings found!");
        } else {
            return trainings;
        }
    }

    @Override
    public Optional<Training> findById(Long id) {
        Optional<Training> training = trainingRepository.findById(id);
        if (training.isEmpty()) {
            throw new OperationException("No Training found for this ID!");
        } else {
            return training;
        }
    }

    @Override
    public Training update(Training trainingUpdated, Long id) {
        Training existingTraining = trainingRepository.findById(id)
                .orElseThrow(() -> new OperationException("Training not found with id: " + id));

        if (trainingUpdated.getDuration() == null || trainingUpdated.getDuration() <= 0) {
            throw new OperationException("Training duration must be greater than 0");
        }
        if (trainingUpdated.getStartDate() == null || trainingUpdated.getStartDate().isBefore(LocalDateTime.now())) {
            throw new OperationException("Training start date must be in the future or present");
        }
        if (trainingUpdated.getLocation() == null || trainingUpdated.getLocation().isEmpty()) {
            throw new OperationException("Training location cannot be empty");
        }
        if (trainingUpdated.getMaxPlaces() == null || trainingUpdated.getMaxPlaces() <= 0) {
            throw new OperationException("Training max places must be greater than 0");
        }

        existingTraining.setTitle(trainingUpdated.getTitle());
        existingTraining.setDescription(trainingUpdated.getDescription());
        existingTraining.setDuration(trainingUpdated.getDuration());
        existingTraining.setStartDate(trainingUpdated.getStartDate());
        existingTraining.setLocation(trainingUpdated.getLocation());
        existingTraining.setMaxPlaces(trainingUpdated.getMaxPlaces());

        return trainingRepository.save(existingTraining);
    }

    @Override
    public void delete(Long id) {
        Training existingTraining = trainingRepository.findById(id)
                .orElseThrow(() -> new OperationException("Training not found with id: " + id));
        trainingRepository.delete(existingTraining);
    }

    @Override
    public List<Training> findByTitleContaining(String keyword) {
        List<Training> trainings = trainingRepository.findByTitleContainingKeyword(keyword);
        if (trainings.isEmpty()) {
            throw new OperationException("No Trainings found with title containing: " + keyword);
        } else {
            return trainings;
        }
    }
}
