package com.example.carrer_bridge.web.rest;


import com.example.carrer_bridge.domain.entities.Communication;
import com.example.carrer_bridge.dto.request.CommunicationRequestDto;
import com.example.carrer_bridge.dto.response.CommunicationResponseDto;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.CommunicationMapper;
import com.example.carrer_bridge.service.CommunicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/communications")
public class CommunicationRest {

    private final CommunicationService communicationService;
    private final CommunicationMapper communicationMapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('VIEW_COMMUNICATION')")
    public ResponseEntity<List<CommunicationResponseDto>> getAllCommunications() {
        List<Communication> communications = communicationService.findAll();
        List<CommunicationResponseDto> communicationResponseDtos = communications.stream().map(communicationMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(communicationResponseDtos);
    }

    @GetMapping("/{communicationId}")
    @PreAuthorize("hasAnyAuthority('VIEW_COMMUNICATION')")
    public ResponseEntity<?> getCommunicationById(@PathVariable Long communicationId) {
        Communication communication = communicationService.findById(communicationId)
                .orElseThrow(() -> new OperationException("Communication not found with ID: " + communicationId));
        CommunicationResponseDto communicationResponseDto = communicationMapper.toResponseDto(communication);
        return ResponseEntity.ok(communicationResponseDto);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage> addCommunication(@Valid @RequestBody CommunicationRequestDto communicationRequestDto) {
        Communication communication = communicationService.save(communicationMapper.fromRequestDto(communicationRequestDto));
        if (communication == null) {
            return ResponseMessage.badRequest("Communication not created");
        } else {
            return ResponseMessage.created("Communication created successfully", communication);
        }
    }

    @PutMapping("/update/{communicationId}")
    public ResponseEntity<ResponseMessage> updateCommunication(@PathVariable Long communicationId, @Valid @RequestBody CommunicationRequestDto communicationRequestDto) {
        Communication updatedCommunication = communicationMapper.fromRequestDto(communicationRequestDto);
        Communication communication = communicationService.update(updatedCommunication, communicationId);
        return ResponseEntity.ok(ResponseMessage.created("Communication updated successfully", communication).getBody());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('DELETE_COMMUNICATION')")
    public ResponseEntity<ResponseMessage> deleteCommunication(@PathVariable Long id) {
        Optional<Communication> existingCommunication = communicationService.findById(id);
        if (existingCommunication.isEmpty()) {
            return ResponseMessage.notFound("Communication not found with ID: " + id);
        }
        communicationService.delete(id);
        return ResponseMessage.ok("Communication deleted successfully with ID: " + id, null);
    }
    @GetMapping("/process/{subject}")
    public ResponseEntity<String> processCommunicationBySubject(@PathVariable String subject) {
        String message = communicationService.processCommunicationBySubject(subject);
        return ResponseEntity.ok(message);
    }
}
