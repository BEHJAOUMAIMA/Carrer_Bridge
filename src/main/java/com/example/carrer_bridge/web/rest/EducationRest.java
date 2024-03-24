package com.example.carrer_bridge.web.rest;

import com.example.carrer_bridge.domain.entities.Education;
import com.example.carrer_bridge.dto.request.UserEducationRequest;
import com.example.carrer_bridge.dto.response.UserEducationResponse;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.UserEducationMapper;
import com.example.carrer_bridge.service.EducationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/educations")
public class EducationRest {
    private final EducationService educationService;
    private final UserEducationMapper userEducationMapper;

    @GetMapping
    public ResponseEntity<List<UserEducationResponse>> getAllEducations() {
        List<Education> educations = educationService.findAll();
        List<UserEducationResponse> educationResponseDtos = educations.stream().map(userEducationMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(educationResponseDtos);
    }

    @GetMapping("/{educationId}")
    public ResponseEntity<?> getEducationById(@PathVariable Long educationId) {
        Education education = educationService.findById(educationId)
                .orElseThrow(() -> new OperationException("Education not found with ID: " + educationId));
        UserEducationResponse educationResponseDto = userEducationMapper.toResponseDto(education);
        return ResponseEntity.ok(educationResponseDto);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage> addEducation(@Valid @RequestBody UserEducationRequest userEducationRequest) {
        educationService.save(userEducationMapper.fromRequestDto(userEducationRequest));
        return ResponseMessage.created("Education created successfully", null);
    }

    @PutMapping("/update/{educationId}")
    public ResponseEntity<ResponseMessage> updateEducation(@PathVariable Long educationId, @Valid @RequestBody UserEducationRequest educationRequest) {
        Education updatedEducation = userEducationMapper.fromRequestDto(educationRequest);
        educationService.update(updatedEducation, educationId);
        return ResponseEntity.ok(ResponseMessage.ok("Education updated successfully", null).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteEducation(@PathVariable Long id) {
        Optional<Education> existingEducation = educationService.findById(id);
        if (existingEducation.isEmpty()) {
            return ResponseMessage.notFound("Education not found with ID: " + id);
        }
        educationService.delete(id);
        return ResponseMessage.ok("Education deleted successfully with ID: " + id, null);
    }
    @GetMapping("/findByName/{degree}")
    public ResponseEntity<List<UserEducationResponse>> findEducationWithDegree(@PathVariable String degree) {
        List<Education> educations = educationService.findEducationByDegree(degree);
        List<UserEducationResponse> educationResponseDtos = educations.stream().map(userEducationMapper::toResponseDto).toList();
        return ResponseEntity.ok(educationResponseDtos);
    }

    @GetMapping("/findByUser")
    public ResponseEntity<List<UserEducationResponse>> findEducationsByUser() {
        List<Education> educations = educationService.findEducationsByUser();
        List<UserEducationResponse> educationResponseDtos = educations.stream().map(userEducationMapper::toResponseDto).toList();
        return ResponseEntity.ok(educationResponseDtos);
    }
}
