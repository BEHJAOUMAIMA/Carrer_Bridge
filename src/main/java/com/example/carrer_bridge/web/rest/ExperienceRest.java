package com.example.carrer_bridge.web.rest;

import com.example.carrer_bridge.domain.entities.Experience;
import com.example.carrer_bridge.dto.request.UserExperienceRequest;
import com.example.carrer_bridge.dto.response.UserExperienceResponse;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.UserExperienceMapper;
import com.example.carrer_bridge.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/experiences")
public class ExperienceRest {

    private final ExperienceService experienceService;
    private final UserExperienceMapper userExperienceMapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('VIEW_EXPERIENCE')")
    public ResponseEntity<List<UserExperienceResponse>> getAllExperiences() {
        List<Experience> experiences = experienceService.findAll();
        List<UserExperienceResponse> experienceResponseDtos = experiences.stream().map(userExperienceMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(experienceResponseDtos);
    }

    @GetMapping("/{experienceId}")
    @PreAuthorize("hasAnyAuthority('VIEW_EXPERIENCE')")
    public ResponseEntity<?> getExperienceById(@PathVariable Long experienceId) {
        Experience experience = experienceService.findById(experienceId)
                .orElseThrow(() -> new OperationException("Experience not found with ID: " + experienceId));
        UserExperienceResponse experienceResponseDto = userExperienceMapper.toResponseDto(experience);
        return ResponseEntity.ok(experienceResponseDto);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('CREATE_EXPERIENCE')")
    public ResponseEntity<ResponseMessage> addExperience(@Valid @RequestBody UserExperienceRequest userExperienceRequest) {
        experienceService.save(userExperienceMapper.fromRequestDto(userExperienceRequest));
        return ResponseMessage.created("Experience created successfully", null);
    }

    @PutMapping("/update/{experienceId}")
    @PreAuthorize("hasAnyAuthority('UPDATE_EXPERIENCE')")
    public ResponseEntity<ResponseMessage> updateExperience(@PathVariable Long experienceId, @Valid @RequestBody UserExperienceRequest experienceRequest) {
        Experience updatedExperience = userExperienceMapper.fromRequestDto(experienceRequest);
        experienceService.update(updatedExperience, experienceId);
        return ResponseEntity.ok(ResponseMessage.ok("Experience updated successfully", null).getBody());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('DELETE_EXPERIENCE')")
    public ResponseEntity<ResponseMessage> deleteExperience(@PathVariable Long id) {
        Optional<Experience> existingExperience = experienceService.findById(id);
        if (existingExperience.isEmpty()) {
            return ResponseMessage.notFound("Experience not found with ID: " + id);
        }
        experienceService.delete(id);
        return ResponseMessage.ok("Experience deleted successfully with ID: " + id, null);
    }
    @GetMapping("/findByTitle/{title}")
    @PreAuthorize("hasAnyAuthority('VIEW_EXPERIENCE')")
    public ResponseEntity<List<UserExperienceResponse>> findExperienceWithDegree(@PathVariable String title) {
        List<Experience> experiences = experienceService.findExperienceByTitle(title);
        List<UserExperienceResponse> experienceResponseDtos = experiences.stream().map(userExperienceMapper::toResponseDto).toList();
        return ResponseEntity.ok(experienceResponseDtos);
    }

    @GetMapping("/findByUser")
    @PreAuthorize("hasAnyAuthority('VIEW_EXPERIENCE')")
    public ResponseEntity<List<UserExperienceResponse>> findExperiencesByUser() {
        List<Experience> experiences = experienceService.findExperiencesByUser();
        List<UserExperienceResponse> experienceResponseDtos = experiences.stream().map(userExperienceMapper::toResponseDto).toList();
        return ResponseEntity.ok(experienceResponseDtos);
    }


}
