package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Communication;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.CommunicationRepository;
import com.example.carrer_bridge.service.CommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommunicationServiceImpl implements CommunicationService {

    private final CommunicationRepository communicationRepository;
    private static final Integer MAX_COMMUNICATIONS_ALLOWED = 3;
    @Override
    public Communication save(Communication communication) {
        if (communicationRepository.countByFullNameAndEmail(communication.getFullName(), communication.getEmail()) >= MAX_COMMUNICATIONS_ALLOWED) {
            throw new OperationException("Max number of communications reached for this user");
        }
        return communicationRepository.save(communication);
    }

    @Override
    public List<Communication> findAll() {
        List<Communication> communications = communicationRepository.findAll();
        if (communications.isEmpty()) {
            throw new OperationException("No Communications found!");
        } else {
            return communications;
        }
    }

    @Override
    public Optional<Communication> findById(Long id) {
        Optional<Communication> communication = communicationRepository.findById(id);
        if (communication.isEmpty()) {
            throw new OperationException("No Communication found for this ID!");
        } else {
            return communication;
        }
    }

    @Override
    public Communication update(Communication communicationUpdated, Long id) {
        Optional<Communication> optionalCommunication = communicationRepository.findById(id);
        if (optionalCommunication.isPresent()) {
            Communication communication = optionalCommunication.get();
            communication.setFullName(communicationUpdated.getFullName());
            communication.setEmail(communicationUpdated.getEmail());
            communication.setCompany(communicationUpdated.getCompany());
            communication.setNumber(communicationUpdated.getNumber());
            communication.setSubject(communicationUpdated.getSubject());
            communication.setMessage(communicationUpdated.getMessage());
            return communicationRepository.save(communication);
        } else {
            throw new OperationException("Communication not found with ID: " + id);
        }
    }
    @Override
    public void delete(Long id) {
        Communication existingCommunication = communicationRepository.findById(id)
                .orElseThrow(() -> new OperationException("Communication not found with id: " + id));
        communicationRepository.delete(existingCommunication);
    }

    @Override
    public String processCommunicationBySubject(String subject) {
        Optional<Communication> communicationOptional = communicationRepository.findBySubject(subject);
        return communicationOptional.map(communication -> {
            String message = communication.getMessage();
            return "Content of the communication with the subject \"" + subject + "\" : " + message;
        }).orElse("No communication found with subject: " + subject);
    }

}
