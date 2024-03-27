package com.example.carrer_bridge.test;

import com.example.carrer_bridge.domain.entities.Experience;
import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.ExperienceRepository;
import com.example.carrer_bridge.repository.UserRepository;
import com.example.carrer_bridge.service.impl.ExperienceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExperienceServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ExperienceRepository experienceRepository;

    @InjectMocks
    private ExperienceServiceImpl experienceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveExperience_Success() {
        // Scenario 1: Saving new experience successfully
        User user = new User();
        user.setId(1L);
        user.setRole(Role.builder().roleType(RoleType.PROFESSIONAL).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Experience experience = new Experience();
        experience.setTitle("Software Engineer");
        experience.setDescription("Developing web applications");
        experience.setStartDate(LocalDate.parse("2022-01-01"));

        when(userRepository.findByIdWithExperiences(1L)).thenReturn(Optional.of(user));
        when(experienceRepository.findByUserIdAndTitle(1L, "Software Engineer")).thenReturn(Optional.empty());
        when(experienceRepository.save(any(Experience.class))).thenReturn(experience);

        Experience savedExperience = experienceService.save(experience);
        assertEquals("Software Engineer", savedExperience.getTitle());
    }

    @Test
    void testSaveExperience_WithTitleEmpty() {
        // Scenario 2: Saving experience with empty title
        User user = new User();
        user.setId(1L);
        user.setRole(Role.builder().roleType(RoleType.PROFESSIONAL).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Experience experience = new Experience();
        experience.setDescription("Developing web applications");
        experience.setStartDate(LocalDate.parse("2022-01-01"));

        when(userRepository.findByIdWithExperiences(1L)).thenReturn(Optional.of(user));

        assertThrows(OperationException.class, () -> experienceService.save(experience));
    }

    @Test
    void testSaveExperience_WithUserNotProfessional() {
        // Scenario 3: Saving experience with non-professional user
        User user = new User();
        user.setId(1L);
        user.setRole(Role.builder().roleType(RoleType.PROFESSIONAL).build()); // Non-professional role
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Experience experience = new Experience();
        experience.setTitle("Software Engineer");
        experience.setDescription("Developing web applications");
        experience.setStartDate(LocalDate.parse("2022-01-01"));

        when(userRepository.findByIdWithExperiences(1L)).thenReturn(Optional.of(user));

        assertThrows(OperationException.class, () -> experienceService.save(experience));
    }

    @Test
    void testSaveExperience_WithDuplicateTitleForUser() {
        // Scenario 4: Saving experience with a duplicate title for the same user
        User user = new User();
        user.setId(1L);
        user.setRole(Role.builder().roleType(RoleType.PROFESSIONAL).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Experience existingExperience = new Experience();
        existingExperience.setId(2L);
        existingExperience.setTitle("Software Engineer");
        existingExperience.setDescription("Developing web applications");
        existingExperience.setStartDate(LocalDate.parse("2021-01-01"));

        Experience newExperience = new Experience();
        newExperience.setTitle("Software Engineer"); // Same title as existing experience
        newExperience.setDescription("Developing mobile applications");
        newExperience.setStartDate(LocalDate.parse("2022-01-01"));

        when(userRepository.findByIdWithExperiences(1L)).thenReturn(Optional.of(user));
        when(experienceRepository.findByUserIdAndTitle(1L, "Software Engineer")).thenReturn(Optional.of(existingExperience));

        assertThrows(OperationException.class, () -> experienceService.save(newExperience));
    }

    @Test
    void testFindAllExperiences_Success() {
        // Scenario 5: Finding all experiences successfully
        List<Experience> experiences = new ArrayList<>();
        experiences.add(new Experience());
        when(experienceRepository.findAll()).thenReturn(experiences);

        List<Experience> result = experienceService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllExperiences_EmptyList() {
        // Scenario 6: No experiences found
        when(experienceRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(OperationException.class, () -> experienceService.findAll());
    }

    @Test
    void testFindExperienceById_Success() {
        // Scenario 7: Finding experience by ID successfully
        Experience experience = new Experience();
        experience.setId(1L);
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));

        Optional<Experience> result = experienceService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testFindExperienceById_NotFound() {
        // Scenario 8: Experience not found by ID
        when(experienceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> experienceService.findById(1L));
    }

    @Test
    void testUpdateExperience_Success() {
        // Scenario 9: Updating experience successfully
        User user = new User();
        user.setId(1L);
        user.setRole(Role.builder().roleType(RoleType.PROFESSIONAL).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Experience existingExperience = new Experience();
        existingExperience.setId(1L);
        existingExperience.setTitle("Software Engineer");
        existingExperience.setDescription("Developing web applications");
        existingExperience.setStartDate(LocalDate.parse("2021-01-01"));

        Experience updatedExperience = new Experience();
        updatedExperience.setTitle("Senior Software Engineer");
        updatedExperience.setDescription("Developing enterprise-grade web applications");
        updatedExperience.setStartDate(LocalDate.parse("2022-01-01"));

        when(experienceRepository.findById(1L)).thenReturn(Optional.of(existingExperience));
        when(userRepository.findByIdWithExperiences(1L)).thenReturn(Optional.of(user));
        when(experienceRepository.findByUserIdAndTitle(1L, "Senior Software Engineer")).thenReturn(Optional.empty());
        when(experienceRepository.save(any(Experience.class))).thenReturn(updatedExperience);

        Experience result = experienceService.update(updatedExperience, 1L);
        assertEquals("Senior Software Engineer", result.getTitle());
    }

    @Test
    void testUpdateExperience_NotAuthorized() {
        // Scenario 10: User not authorized to update experience
        User user = new User();
        user.setId(1L);
        user.setRole(Role.builder().roleType(RoleType.PROFESSIONAL).build()); // Non-professional role
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Experience experience = new Experience();
        experience.setId(1L);

        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));

        assertThrows(OperationException.class, () -> experienceService.update(new Experience(), 1L));
    }

    @Test
    void testDeleteExperience_Success() {
        // Scenario 11: Deleting experience successfully
        Experience experience = new Experience();
        experience.setId(1L);
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));

        experienceService.delete(1L);
        verify(experienceRepository, times(1)).delete(experience);
    }

    @Test
    void testDeleteExperience_NotFound() {
        // Scenario 12: Experience not found for deletion
        when(experienceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> experienceService.delete(1L));
    }
}
