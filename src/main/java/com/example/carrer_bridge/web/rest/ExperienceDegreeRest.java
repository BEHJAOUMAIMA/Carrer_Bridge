package com.example.carrer_bridge.web.rest;

import com.example.carrer_bridge.domain.entities.ExperienceDegree;
import com.example.carrer_bridge.dto.request.ExperienceDegreeRequestDto;
import com.example.carrer_bridge.dto.response.ExperienceDegreeResponseDto;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.ExperienceDegreeMapper;
import com.example.carrer_bridge.service.ExperienceDegreeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/experience-degrees")
public class ExperienceDegreeRest {

    private final ExperienceDegreeService experienceDegreeService;
    private final ExperienceDegreeMapper experienceDegreeMapper;

    @GetMapping
    public ResponseEntity<List<ExperienceDegreeResponseDto>> getAllExperienceDegrees() {
        List<ExperienceDegree> experienceDegrees = experienceDegreeService.findAll();
        List<ExperienceDegreeResponseDto> experienceDegreeResponseDtos = experienceDegrees.stream().map(experienceDegreeMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(experienceDegreeResponseDtos);
    }

    @GetMapping("/{experienceDegreeId}")
    public ResponseEntity<?> getExperienceDegreeById(@PathVariable Long experienceDegreeId) {
        ExperienceDegree experienceDegree = experienceDegreeService.findById(experienceDegreeId)
                .orElseThrow(() -> new OperationException("Experience Degree not found with ID: " + experienceDegreeId));
        ExperienceDegreeResponseDto experienceDegreeResponseDto = experienceDegreeMapper.toResponseDto(experienceDegree);
        return ResponseEntity.ok(experienceDegreeResponseDto);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage> addExperienceDegree(@Valid @RequestBody ExperienceDegreeRequestDto experienceDegreeRequestDto) {
        ExperienceDegree experienceDegree = experienceDegreeService.save(experienceDegreeMapper.fromRequestDto(experienceDegreeRequestDto));
        if (experienceDegree == null) {
            return ResponseMessage.badRequest("Experience Degree not created");
        } else {
            return ResponseMessage.created("Experience Degree created successfully", experienceDegree);
        }
    }

    @PutMapping("/update/{experienceDegreeId}")
    public ResponseEntity<ResponseMessage> updateExperienceDegree(@PathVariable Long experienceDegreeId, @Valid @RequestBody ExperienceDegreeRequestDto experienceDegreeRequestDto) {
        ExperienceDegree updatedExperienceDegree = experienceDegreeMapper.fromRequestDto(experienceDegreeRequestDto);
        ExperienceDegree experienceDegree = experienceDegreeService.update(updatedExperienceDegree, experienceDegreeId);
        return ResponseEntity.ok(ResponseMessage.created("Experience Degree updated successfully", experienceDegree).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteExperienceDegree(@PathVariable Long id) {
        Optional<ExperienceDegree> existingExperienceDegree = experienceDegreeService.findById(id);
        if (existingExperienceDegree.isEmpty()) {
            return ResponseMessage.notFound("Experience Degree not found with ID: " + id);
        }
        experienceDegreeService.delete(id);
        return ResponseMessage.ok("Experience Degree  deleted successfully with ID: " + id, null);
    }
}
