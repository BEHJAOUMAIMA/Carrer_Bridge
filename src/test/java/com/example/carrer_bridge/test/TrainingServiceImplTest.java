package com.example.carrer_bridge.test;

import com.example.carrer_bridge.domain.entities.Training;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.repository.TrainingRepository;
import com.example.carrer_bridge.repository.UserRepository;
import com.example.carrer_bridge.service.impl.TrainingServiceImpl;

import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TrainingServiceImplTest {

    @Test
    public void test_saveTrainingWithValidData() {

        TrainingRepository trainingRepository = mock(TrainingRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        TrainingServiceImpl trainingService = new TrainingServiceImpl(trainingRepository, userRepository);
        Training training = new Training();
        training.setTitle("Test Training");
        training.setDescription("Test Description");
        training.setDuration(60);
        training.setStartDate(LocalDateTime.now().plusDays(1));
        training.setLocation("Test Location");
        training.setMaxPlaces(10L);
    
        User authenticatedUser = new User();
        authenticatedUser.setId(1L);
    
        when(trainingRepository.findByTitleAndUser(anyString(), any(User.class))).thenReturn(Collections.emptyList());
        when(trainingRepository.save(any(Training.class))).thenReturn(training);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(authenticatedUser);
    
        Training result = trainingService.save(training);
    
        assertEquals(training, result);
        verify(trainingRepository, times(1)).findByTitleAndUser(anyString(), any(User.class));
        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    public void test_findAllTrainings() {

        TrainingRepository trainingRepository = mock(TrainingRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        TrainingServiceImpl trainingService = new TrainingServiceImpl(trainingRepository, userRepository);
        List<Training> trainings = new ArrayList<>();
        trainings.add(new Training());
        trainings.add(new Training());
    
        when(trainingRepository.findAll()).thenReturn(trainings);
    
        List<Training> result = trainingService.findAll();
    
        assertEquals(trainings, result);
        verify(trainingRepository, times(1)).findAll();
    }

    @Test
    public void test_findTrainingById() {

        TrainingRepository trainingRepository = mock(TrainingRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        TrainingServiceImpl trainingService = new TrainingServiceImpl(trainingRepository, userRepository);
        Long trainingId = 1L;
        Training training = new Training();
    
        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));
    
        // Act
        Optional<Training> result = trainingService.findById(trainingId);
    
        // Assert
        assertTrue(result.isPresent());
        assertEquals(training, result.get());
        verify(trainingRepository, times(1)).findById(trainingId);
    }

    // Updating a training with valid data should return the updated training object.
    @Test
    public void test_updateTrainingWithValidData() {
        // Arrange
        TrainingRepository trainingRepository = mock(TrainingRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        TrainingServiceImpl trainingService = new TrainingServiceImpl(trainingRepository, userRepository);
        Long trainingId = 1L;
        Training existingTraining = new Training();
        existingTraining.setId(trainingId);
        existingTraining.setTitle("Existing Training");
        existingTraining.setDescription("Existing Description");
        existingTraining.setDuration(60);
        existingTraining.setStartDate(LocalDateTime.now().plusDays(1));
        existingTraining.setLocation("Existing Location");
        existingTraining.setMaxPlaces(10L);

        Training updatedTraining = new Training();
        updatedTraining.setId(trainingId);
        updatedTraining.setTitle("Updated Training");
        updatedTraining.setDescription("Updated Description");
        updatedTraining.setDuration(90);
        updatedTraining.setStartDate(LocalDateTime.now().plusDays(2));
        updatedTraining.setLocation("Updated Location");
        updatedTraining.setMaxPlaces(15L);

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(existingTraining));
        when(trainingRepository.save(any(Training.class))).thenReturn(updatedTraining);

        // Act
        Training result = trainingService.update(updatedTraining, trainingId);

        // Assert
        assertEquals(updatedTraining, result);
        verify(trainingRepository, times(1)).findById(trainingId);
        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    // Deleting a training should remove the training from the database.
    @Test
    public void test_deleteTraining() {
        // Arrange
        TrainingRepository trainingRepository = mock(TrainingRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        TrainingServiceImpl trainingService = new TrainingServiceImpl(trainingRepository, userRepository);
        Long trainingId = 1L;
        Training existingTraining = new Training();
        existingTraining.setId(trainingId);
        existingTraining.setTitle("Existing Training");
        existingTraining.setDescription("Existing Description");
        existingTraining.setDuration(60);
        existingTraining.setStartDate(LocalDateTime.now().plusDays(1));
        existingTraining.setLocation("Existing Location");
        existingTraining.setMaxPlaces(10L);

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(existingTraining));

        // Act
        trainingService.delete(trainingId);

        // Assert
        verify(trainingRepository, times(1)).findById(trainingId);
        verify(trainingRepository, times(1)).delete(existingTraining);
    }

    // Searching for trainings by title containing a keyword should return a list of matching trainings.
    @Test
    public void test_findTrainingsByTitleContainingKeyword() {
        // Arrange
        TrainingRepository trainingRepository = mock(TrainingRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        TrainingServiceImpl trainingService = new TrainingServiceImpl(trainingRepository, userRepository);
        String keyword = "Java";
        List<Training> matchingTrainings = new ArrayList<>();
        matchingTrainings.add(new Training());
        matchingTrainings.add(new Training());

        when(trainingRepository.findByTitleContainingKeyword(keyword)).thenReturn(matchingTrainings);

        // Act
        List<Training> result = trainingService.findByTitleContaining(keyword);

        // Assert
        assertEquals(matchingTrainings, result);
        verify(trainingRepository, times(1)).findByTitleContainingKeyword(keyword);
    }

}