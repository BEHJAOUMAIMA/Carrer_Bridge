package com.example.carrer_bridge.web.rest;

import com.example.carrer_bridge.domain.entities.Training;
import com.example.carrer_bridge.dto.request.TrainingRequestDto;
import com.example.carrer_bridge.dto.response.TrainingResponseDto;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.TrainingMapper;
import com.example.carrer_bridge.service.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trainings")
public class TrainingRest {

    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;

    @GetMapping
    public ResponseEntity<List<TrainingResponseDto>> getAllTrainings() {
        List<Training> trainings = trainingService.findAll();
        List<TrainingResponseDto> trainingResponseDtos = trainings.stream().map(trainingMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(trainingResponseDtos);
    }
    @GetMapping("/get")
    public ResponseEntity<List<TrainingResponseDto>> getAllTrainingsWithoutAuth() {
        List<Training> trainings = trainingService.getTraining();
        List<TrainingResponseDto> trainingResponseDtos = trainings.stream().map(trainingMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(trainingResponseDtos);
    }
    @GetMapping("/user")
    public ResponseEntity<List<Training>> getTrainingsForCurrentUser() {
        List<Training> trainings = trainingService.findTrainingsForCurrentUser();
        return new ResponseEntity<>(trainings, HttpStatus.OK);
    }

    @GetMapping("/{trainingId}")
    public ResponseEntity<?> getTrainingById(@PathVariable Long trainingId) {
        Training training = trainingService.findById(trainingId)
                .orElseThrow(() -> new OperationException("Training not found with ID: " + trainingId));
        TrainingResponseDto trainingResponseDto = trainingMapper.toResponseDto(training);
        return ResponseEntity.ok(trainingResponseDto);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('CREATE_TRAINING')")
    public ResponseEntity<ResponseMessage> addTraining(@Valid @RequestBody TrainingRequestDto trainingRequestDto) {
        Training training = trainingService.save(trainingMapper.fromRequestDto(trainingRequestDto));
        if (training == null) {
            return ResponseMessage.badRequest("Training not created");
        } else {
            return ResponseMessage.created("Training created successfully", training);
        }
    }

    @PutMapping("/update/{trainingId}")
    @PreAuthorize("hasAnyAuthority('UPDATE_TRAINING')")
    public ResponseEntity<ResponseMessage> updateTraining(@PathVariable Long trainingId, @Valid @RequestBody TrainingRequestDto trainingRequestDto) {
        Training updatedTraining = trainingMapper.fromRequestDto(trainingRequestDto);
        Training training = trainingService.update(updatedTraining, trainingId);
        return ResponseEntity.ok(ResponseMessage.created("Training updated successfully", training).getBody());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('DELETE_TRAINING')")
    public ResponseEntity<ResponseMessage> deleteTraining(@PathVariable Long id) {
        Optional<Training> existingTraining = trainingService.findById(id);
        if (existingTraining.isEmpty()) {
            return ResponseMessage.notFound("Training not found with ID: " + id);
        }
        trainingService.delete(id);
        return ResponseMessage.ok("Training deleted successfully with ID: " + id, null);
    }
    @GetMapping("/find-trainings")
    public ResponseEntity<List<TrainingResponseDto>> findTrainingsByTitleContaining(@RequestParam String keyword) {
        List<Training> trainings = trainingService.findByTitleContaining(keyword);
        List<TrainingResponseDto> trainingResponseDtos = trainings.stream().map(trainingMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(trainingResponseDtos);
    }
    @PostMapping("/register/{trainingId}")
    public ResponseEntity<String> registerToTraining(@PathVariable Long trainingId) {
        String message = trainingService.registerToTraining(trainingId);
        return ResponseEntity.ok(message);
    }
}
