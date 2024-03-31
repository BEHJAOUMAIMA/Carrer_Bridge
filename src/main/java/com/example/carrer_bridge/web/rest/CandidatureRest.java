package com.example.carrer_bridge.web.rest;

import com.example.carrer_bridge.domain.entities.Candidature;
import com.example.carrer_bridge.domain.enums.CandidatureStatus;
import com.example.carrer_bridge.dto.request.CandidatureRequestDto;
import com.example.carrer_bridge.dto.response.CandidatureResponseDTO;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.CandidatureMapper;
import com.example.carrer_bridge.service.CandidatureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/candidatures")
public class CandidatureRest {


    private final CandidatureService candidatureService;
    private final CandidatureMapper candidatureMapper;

    @GetMapping
    public ResponseEntity<List<CandidatureResponseDTO>> getAllCandidatures() {
        List<Candidature> candidatures = candidatureService.findAll();
        List<CandidatureResponseDTO> candidatureResponseDtos = candidatures.stream().map(candidatureMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(candidatureResponseDtos);
    }

    @GetMapping("/{candidatureId}")
    public ResponseEntity<?> getCandidatureById(@PathVariable Long candidatureId) {
        Candidature candidature = candidatureService.findById(candidatureId)
                .orElseThrow(() -> new OperationException("Candidature not found with ID: " + candidatureId));
        CandidatureResponseDTO candidatureResponseDto = candidatureMapper.toResponseDto(candidature);
        return ResponseEntity.ok(candidatureResponseDto);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage> addCandidature(@Valid @RequestBody CandidatureRequestDto candidatureRequestDto) {
        Candidature candidature = candidatureRequestDto.toCandidature();
        if (candidature.getJobOpportunity() == null || candidature.getJobOpportunity().getId() == null) {
            return ResponseMessage.badRequest("Job Opportunity ID cannot be null");
        }

        candidature.setApplicationDate(LocalDateTime.now());
        candidature.setStatus(CandidatureStatus.PENDING);

        Candidature savedCandidature = candidatureService.save(candidature);
        if (savedCandidature == null) {
            return ResponseMessage.badRequest("Candidature not created");
        } else {
            return ResponseMessage.created("Candidature created successfully", savedCandidature);
        }
    }

    @PutMapping("/update/{candidatureId}")
    public ResponseEntity<ResponseMessage> updateCandidature(@PathVariable Long candidatureId, @Valid @RequestBody CandidatureRequestDto candidatureRequestDto) {
        Candidature updatedCandidature = candidatureMapper.fromRequestDto(candidatureRequestDto);
        Candidature candidature = candidatureService.update(updatedCandidature, candidatureId);
        return ResponseEntity.ok(ResponseMessage.created("Candidature updated successfully", candidature).getBody());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteCandidature(@PathVariable Long id) {
        Optional<Candidature> existingCandidature = candidatureService.findById(id);
        if (existingCandidature.isEmpty()) {
            return ResponseMessage.notFound("Candidature not found with ID: " + id);
        }
        candidatureService.delete(id);
        return ResponseMessage.ok("Candidature deleted successfully with ID: " + id, null);
    }
    @GetMapping("/find-by-user/{userId}")
    public ResponseEntity<List<CandidatureResponseDTO>> getCandidaturesByUserId(@PathVariable Long userId) {
        List<Candidature> candidatures = candidatureService.findByUserId(userId);
        List<CandidatureResponseDTO> candidatureResponseDtos = candidatures.stream().map(candidatureMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(candidatureResponseDtos);
    }

    @GetMapping("/find-by-job/{jobId}")
    public ResponseEntity<List<CandidatureResponseDTO>> getCandidaturesByJobOpportunityId(@PathVariable Long jobId) {
        List<Candidature> candidatures = candidatureService.findByJobOpportunityId(jobId);
        List<CandidatureResponseDTO> candidatureResponseDtos = candidatures.stream().map(candidatureMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(candidatureResponseDtos);
    }

    @GetMapping("/find-by-status/{status}")
    public ResponseEntity<List<CandidatureResponseDTO>> getCandidaturesByStatus(@PathVariable CandidatureStatus status) {
        List<Candidature> candidatures = candidatureService.findByStatus(status);
        List<CandidatureResponseDTO> candidatureResponseDtos = candidatures.stream().map(candidatureMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(candidatureResponseDtos);
    }

    @PostMapping("/accept/{candidatureId}")
    public ResponseEntity<ResponseMessage> acceptCandidature(@PathVariable Long candidatureId) {
        candidatureService.acceptCandidature(candidatureId);
        return ResponseMessage.ok("Candidature accepted successfully", null);
    }

    @PostMapping("/reject/{candidatureId}")
    public ResponseEntity<ResponseMessage> rejectCandidature(@PathVariable Long candidatureId) {
        candidatureService.refuseCandidature(candidatureId);
        return ResponseMessage.ok("Candidature rejected successfully", null);
    }
}
