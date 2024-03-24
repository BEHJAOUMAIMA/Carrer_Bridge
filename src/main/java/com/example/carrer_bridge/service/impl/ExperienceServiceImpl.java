package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Experience;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.ExperienceRepository;
import com.example.carrer_bridge.repository.UserRepository;
import com.example.carrer_bridge.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {
    private final UserRepository userRepository;
    private final ExperienceRepository experienceRepository;

    @Override
    public Experience save(Experience experience) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authenticatedUser = userRepository.findByIdWithEducation(authenticatedUser.getId())
                .orElseThrow(() -> new OperationException("User not found"));

        if (experience.getTitle() == null || experience.getTitle().isEmpty()) {
            throw new OperationException("Experience Title must not be empty");
        }
        if (experience.getDescription() == null || experience.getDescription().isEmpty()) {
            throw new OperationException("Experience Description must not be null");
        }
        if (experience.getStartDate() == null) {
            throw new OperationException("Start date must not be null");
        }
        if (experience.getEndDate() != null && experience.getEndDate().isBefore(experience.getStartDate())) {
            throw new OperationException("End date must be after start date for Experience");
        }
        if (authenticatedUser.getRole().getRoleType() != RoleType.PROFESSIONAL) {
            throw new OperationException("Only users with role 'Professional' can save Experience");
        }

        Optional<Experience> existingExperience = experienceRepository.findByUserIdAndTitle(authenticatedUser.getId(), experience.getTitle());
        if (existingExperience.isPresent()) {
            throw new OperationException("Experience already exists for this user");
        }

        experience.setUser(authenticatedUser);
        return experienceRepository.save(experience);
    }

    @Override
    public List<Experience> findAll() {
        List<Experience> experiences = experienceRepository.findAll();
        if (experiences.isEmpty()) {
            throw new OperationException("No Experiences found!");
        } else {
            return experiences;
        }
    }

    @Override
    public Optional<Experience> findById(Long id) {
        Optional<Experience> experience = experienceRepository.findById(id);
        if (experience.isEmpty()) {
            throw new OperationException("No Experience found for this ID!");
        } else {
            return experience;
        }
    }

    @Override
    public Experience update(Experience experienceUpdated, Long id) {
        Optional<Experience> existingExperienceOptional = experienceRepository.findById(id);
        if (existingExperienceOptional.isEmpty()) {
            throw new OperationException("Experience not found with ID: " + id);
        }
        Experience existingExperience = existingExperienceOptional.get();

        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authenticatedUser.getRole().getRoleType() != RoleType.PROFESSIONAL) {
            throw new OperationException("Only users with role 'Professional' can update experience");
        }

        if (experienceUpdated.getTitle() == null || experienceUpdated.getTitle().isEmpty()) {
            throw new OperationException("Experience Title must not be null or empty");
        }
        if (experienceUpdated.getDescription() == null || experienceUpdated.getDescription().isEmpty()) {
            throw new OperationException("Experience Description must not be null or empty");
        }
        if (experienceUpdated.getStartDate() == null) {
            throw new OperationException("Start date must not be null");
        }
        if (experienceUpdated.getEndDate() != null && experienceUpdated.getEndDate().isBefore(experienceUpdated.getStartDate())) {
            throw new OperationException("End date must be after start date for Experience");
        }

        Optional<Experience> existingExperienceWithSameTitle = experienceRepository.findByUserIdAndTitle(authenticatedUser.getId(), experienceUpdated.getTitle());
        if (existingExperienceWithSameTitle.isPresent() && !existingExperienceWithSameTitle.get().getId().equals(existingExperience.getId())) {
            throw new OperationException("Another Experience with the same Title already exists");
        }

        if (!existingExperience.getUser().getId().equals(authenticatedUser.getId())) {
            throw new OperationException("You are not authorized to update this Experience");
        }

        existingExperience.setTitle(experienceUpdated.getTitle());
        existingExperience.setDescription(experienceUpdated.getDescription());
        existingExperience.setStartDate(experienceUpdated.getStartDate());
        existingExperience.setEndDate(experienceUpdated.getEndDate());

        return experienceRepository.save(existingExperience);
    }

    @Override
    public void delete(Long id) {
        Experience existingExperience = experienceRepository.findById(id)
                .orElseThrow(() -> new OperationException("Experience not found with id: " + id));
        experienceRepository.delete(existingExperience);
    }

    @Override
    public List<Experience> findExperienceByTitle(String title) {
        List<Experience> experiencesByTitle = experienceRepository.findByTitle(title);
        if (experiencesByTitle.isEmpty()) {
            throw new OperationException("No Experiences found by this name !");
        } else {
            return experiencesByTitle;
        }
    }

    @Override
    public List<Experience> findExperiencesByUser() {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Experience> experiencesByUser = experienceRepository.findByUser(authenticatedUser);
        if (experiencesByUser.isEmpty()) {
            throw new OperationException("No Experiences found for this user !");
        } else {
            return experiencesByUser;
        }
    }
}
