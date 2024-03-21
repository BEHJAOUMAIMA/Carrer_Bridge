package com.example.carrer_bridge.service;

import com.example.carrer_bridge.domain.entities.Communication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CommunicationService {
    Communication save(Communication communication);
    List<Communication> findAll();
    Optional<Communication> findById(Long id);
    Communication update(Communication communicationUpdated, Long id);
    void delete(Long id);
    String processCommunicationBySubject(String subject);
}
