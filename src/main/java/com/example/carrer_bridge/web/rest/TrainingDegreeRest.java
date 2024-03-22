package com.example.carrer_bridge.web.rest;


import com.example.carrer_bridge.domain.entities.TrainingDegree;
import com.example.carrer_bridge.dto.request.TrainingDegreeRequestDto;
import com.example.carrer_bridge.dto.response.TrainingDegreeResponseDto;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.TrainingDegreeMapper;
import com.example.carrer_bridge.service.TrainingDegreeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/training-degrees")
public class TrainingDegreeRest {

    private final TrainingDegreeService trainingDegreeService;
    private final TrainingDegreeMapper trainingDegreeMapper;

    @GetMapping
    public ResponseEntity<List<TrainingDegreeResponseDto>> getAllTrainingDegrees() {
        List<TrainingDegree> trainingDegrees = trainingDegreeService.findAll();
        List<TrainingDegreeResponseDto> trainingDegreeResponseDtos = trainingDegrees.stream().map(trainingDegreeMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(trainingDegreeResponseDtos);
    }

    @GetMapping("/{trainingDegreeId}")
    public ResponseEntity<?> getTrainingDegreeById(@PathVariable Long trainingDegreeId) {
        TrainingDegree trainingDegree = trainingDegreeService.findById(trainingDegreeId)
                .orElseThrow(() -> new OperationException("Training Degree not found with ID: " + trainingDegreeId));
        TrainingDegreeResponseDto trainingDegreeResponseDto = trainingDegreeMapper.toResponseDto(trainingDegree);
        return ResponseEntity.ok(trainingDegreeResponseDto);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage> addTrainingDegree(@Valid @RequestBody TrainingDegreeRequestDto trainingDegreeRequestDto) {
        TrainingDegree trainingDegree = trainingDegreeService.save(trainingDegreeMapper.fromRequestDto(trainingDegreeRequestDto));
        if (trainingDegree == null) {
            return ResponseMessage.badRequest("Training Degree not created");
        } else {
            return ResponseMessage.created("Training Degree created successfully", trainingDegree);
        }
    }

    @PutMapping("/update/{trainingDegreeId}")
    public ResponseEntity<ResponseMessage> updateTrainingDegree(@PathVariable Long trainingDegreeId, @Valid @RequestBody TrainingDegreeRequestDto trainingDegreeRequestDto) {
        TrainingDegree updatedTrainingDegree = trainingDegreeMapper.fromRequestDto(trainingDegreeRequestDto);
        TrainingDegree trainingDegree = trainingDegreeService.update(updatedTrainingDegree, trainingDegreeId);
        return ResponseEntity.ok(ResponseMessage.created("Training Degree updated successfully", trainingDegree).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteTrainingDegree(@PathVariable Long id) {
        Optional<TrainingDegree> existingTrainingDegree = trainingDegreeService.findById(id);
        if (existingTrainingDegree.isEmpty()) {
            return ResponseMessage.notFound("Training Degree not found with ID: " + id);
        }
        trainingDegreeService.delete(id);
        return ResponseMessage.ok("Training Degree  deleted successfully with ID: " + id, null);
    }


}
