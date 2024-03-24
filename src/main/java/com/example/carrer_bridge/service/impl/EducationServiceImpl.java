package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Education;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.EducationRepository;
import com.example.carrer_bridge.repository.UserRepository;
import com.example.carrer_bridge.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService{
    private final EducationRepository educationRepository;
    private final UserRepository userRepository;
    @Override
    public Education save(Education education) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authenticatedUser = userRepository.findByIdWithEducation(authenticatedUser.getId())
                .orElseThrow(() -> new OperationException("User not found"));

        if (education.getDegree() == null || education.getDegree().isEmpty()) {
            throw new OperationException("Education Degree must not be empty");
        }
        if (education.getInstitution() == null || education.getInstitution().isEmpty()) {
            throw new OperationException("Institution name must not be null");
        }
        if (education.getGraduationDate() == null) {
            throw new OperationException("Graduation date must not be null");
        }
        if (authenticatedUser.getRole().getRoleType() != RoleType.PROFESSIONAL) {
            throw new OperationException("Only users with role 'Professional' can save Education");
        }

        Optional<Education> existingEducation = educationRepository.findByUserIdAndDegree(authenticatedUser.getId(), education.getDegree());
        if (existingEducation.isPresent()) {
            throw new OperationException("Education already exists for this user");
        }

        education.setUser(authenticatedUser);
        return educationRepository.save(education);
    }
    @Override
    public List<Education> findAll() {
        List<Education> educations = educationRepository.findAll();
        if (educations.isEmpty()) {
            throw new OperationException("No Educations found!");
        } else {
            return educations;
        }
    }
    @Override
    public Optional<Education> findById(Long id) {
        Optional<Education> education = educationRepository.findById(id);
        if (education.isEmpty()) {
            throw new OperationException("No Education found for this ID!");
        } else {
            return education;
        }
    }
    @Override
    public Education update(Education educationUpdated, Long id) {
        Optional<Education> existingEducationOptional = educationRepository.findById(id);
        if (existingEducationOptional.isEmpty()) {
            throw new OperationException("Education not found with ID: " + id);
        }
        Education existingEducation = existingEducationOptional.get();

        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authenticatedUser.getRole().getRoleType() != RoleType.PROFESSIONAL) {
            throw new OperationException("Only users with role 'Professional' can update education");
        }

        if (educationUpdated.getDegree() == null || educationUpdated.getDegree().isEmpty()) {
            throw new OperationException("Education degree must not be empty");
        }
        if (educationUpdated.getInstitution() == null || educationUpdated.getInstitution().isEmpty()) {
            throw new OperationException("Institution name  must not be null");
        }
        if (educationUpdated.getGraduationDate() == null) {
            throw new OperationException("Graduation date must not be null");
        }

        Optional<Education> existingEducationWithSameName = educationRepository.findByUserIdAndDegree(authenticatedUser.getId(), educationUpdated.getDegree());
        if (existingEducationWithSameName.isPresent() && !existingEducationWithSameName.get().getId().equals(existingEducation.getId())) {
            throw new OperationException("Another Education with the same name already exists");
        }

        if (!existingEducation.getUser().getId().equals(authenticatedUser.getId())) {
            throw new OperationException("You are not authorized to update this Education");
        }

        existingEducation.setDegree(educationUpdated.getDegree());
        existingEducation.setInstitution(educationUpdated.getInstitution());
        existingEducation.setGraduationDate(educationUpdated.getGraduationDate());

        return educationRepository.save(existingEducation);
    }
    @Override
    public void delete(Long id) {
        Education existingEducation = educationRepository.findById(id)
                .orElseThrow(() -> new OperationException("Education not found with id: " + id));
        educationRepository.delete(existingEducation);
    }
    @Override
    public List<Education> findEducationByDegree(String degree) {
        List<Education> educationsByName = educationRepository.findByDegree(degree);
        if (educationsByName.isEmpty()) {
            throw new OperationException("No Educations found by this name !");
        } else {
            return educationsByName;
        }
    }
    @Override
    public List<Education> findEducationsByUser() {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Education> educationsByUser = educationRepository.findByUser(authenticatedUser);
        if (educationsByUser.isEmpty()) {
            throw new OperationException("No Educations found for this user !");
        } else {
            return educationsByUser;
        }
    }
}
