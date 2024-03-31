package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Candidature;
import com.example.carrer_bridge.domain.entities.JobOpportunity;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.enums.CandidatureStatus;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.CandidatureRepository;
import com.example.carrer_bridge.service.CandidatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CandidatureServiceImpl implements CandidatureService {

    private final CandidatureRepository candidatureRepository;
    @Override
    public List<Candidature> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();
        String role = authenticatedUser.getRole().getRoleType().toString();

        return switch (role) {
            case "ADMINISTRATOR" -> candidatureRepository.findAll();
            case "RECRUITER" -> candidatureRepository.findByJobOpportunityUser(authenticatedUser);
            case "PROFESSIONAL" -> candidatureRepository.findByUser(authenticatedUser);
            default -> throw new OperationException("Role not supported: " + role);
        };
    }

    @Override
    public Optional<Candidature> findById(Long id) {
        if (id == null) {
            throw new OperationException("Candidature ID cannot be null.");
        }
        Optional<Candidature> candidature = candidatureRepository.findById(id);
        if (candidature.isEmpty()) {
            throw new OperationException("Candidature not found with ID: " + id);
        }
        return candidature;
    }
    @Override
    public List<Candidature> findByUserId(Long userId) {
        if (userId == null) {
            throw new OperationException("User ID cannot be null.");
        }
        List<Candidature> candidatures = candidatureRepository.findByUserId(userId);
        if (candidatures.isEmpty()) {
            throw new OperationException("No candidatures found for user ID: " + userId);
        }
        return candidatures;
    }
    @Override
    public List<Candidature> findByJobOpportunityId(Long jobOpportunityId) {
        if (jobOpportunityId == null) {
            throw new OperationException("Job opportunity ID cannot be null.");
        }
        List<Candidature> candidatures = candidatureRepository.findByJobOpportunityId(jobOpportunityId);

        if (candidatures.isEmpty()) {
            throw new OperationException("No candidatures found for job opportunity ID: " + jobOpportunityId);
        }
        return candidatures;
    }
    @Override
    public List<Candidature> findByStatus(CandidatureStatus status) {
        if (status == null) {
            throw new OperationException("Candidature status cannot be null.");
        }
        List<Candidature> candidatures = candidatureRepository.findByStatus(status);
        if (candidatures.isEmpty()) {
            throw new OperationException("No candidatures found with status: " + status);
        }
        return candidatures;
    }
    @Override
    public Candidature save(Candidature candidature) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authenticatedUser == null || !authenticatedUser.getRole().getRoleType().equals(RoleType.PROFESSIONAL)) {
            throw new OperationException("Only professionals can submit candidatures.");
        }


        JobOpportunity jobOpportunity = candidature.getJobOpportunity();
        if (jobOpportunity == null) {
            throw new OperationException("Job opportunity must be provided.");
        }

        Long jobOpportunityId = jobOpportunity.getId();
        if (jobOpportunityId == null) {
            throw new OperationException("Job opportunity ID must be provided.");
        }


        List<Candidature> existingCandidatures = candidatureRepository.findByUserIdAndJobOpportunityId(authenticatedUser.getId(), jobOpportunityId);
        if (!existingCandidatures.isEmpty()) {
            throw new OperationException("You have already applied to this job opportunity !");
        }

        candidature.setUser(authenticatedUser);
        candidature.setApplicationDate(LocalDateTime.now());
        candidature.setStatus(CandidatureStatus.PENDING);

        return candidatureRepository.save(candidature);
    }

    public Candidature update(Candidature candidatureUpdated, Long id) {
        Optional<Candidature> optionalCandidature = candidatureRepository.findById(id);
        if (optionalCandidature.isEmpty()) {
            throw new OperationException("Candidature not found with id: " + id);
        }
        Candidature existingCandidature = optionalCandidature.get();

        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authenticatedUser == null || !authenticatedUser.getRole().getRoleType().equals(RoleType.PROFESSIONAL)) {
            throw new OperationException("Only professionals can update candidatures.");
        }

        if (!existingCandidature.getUser().getId().equals(authenticatedUser.getId())) {
            throw new OperationException("You are not authorized to update this candidature.");
        }

        if (existingCandidature.getJobOpportunity().getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new OperationException("This job opportunity has already expired.");
        }

        candidatureUpdated.setStatus(CandidatureStatus.PENDING);

        existingCandidature.setJobOpportunity(candidatureUpdated.getJobOpportunity());
        existingCandidature.setApplicationDate(candidatureUpdated.getApplicationDate());
        existingCandidature.setStatus(candidatureUpdated.getStatus());

        return candidatureRepository.save(existingCandidature);
    }
    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new OperationException("Candidature ID cannot be null.");
        }
        if (!candidatureRepository.existsById(id)) {
            throw new OperationException("Candidature not found with ID: " + id);
        }
        candidatureRepository.deleteById(id);
    }
    @Override
    public void acceptCandidature(Long candidatureId) {
        if (candidatureId == null) {
            throw new OperationException("Candidature cannot be null.");
        }
        Optional<Candidature> optionalCandidature = candidatureRepository.findById(candidatureId);

        if (optionalCandidature.isEmpty()) {
            throw new OperationException("Candidature not found with ID: " + candidatureId);
        }
        Candidature candidature = optionalCandidature.get();
        candidature.setStatus(CandidatureStatus.APPROVED);
        candidatureRepository.save(candidature);
    }
    @Override
    public void refuseCandidature(Long candidatureId) {
        if (candidatureId == null) {
            throw new OperationException("Candidature must not be null.");
        }

        Optional<Candidature> optionalCandidature = candidatureRepository.findById(candidatureId);
        if (optionalCandidature.isEmpty()) {
            throw new OperationException("Candidature not found with ID: " + candidatureId);
        }
        Candidature candidature = optionalCandidature.get();
        candidature.setStatus(CandidatureStatus.REJECTED);
        candidatureRepository.save(candidature);
    }

}
