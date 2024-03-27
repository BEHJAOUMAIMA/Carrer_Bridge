package com.example.carrer_bridge.test;

import com.example.carrer_bridge.domain.entities.Education;
import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.EducationRepository;
import com.example.carrer_bridge.repository.UserRepository;
import com.example.carrer_bridge.service.impl.EducationServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EducationServiceImplTest {

    @Mock
    private EducationRepository educationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EducationServiceImpl educationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEducation_Success() {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.builder().roleType(RoleType.PROFESSIONAL).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Education education = new Education();
        education.setDegree("BSc");
        education.setInstitution("University");
        education.setGraduationDate(LocalDate.parse("2023-01-01"));

        when(userRepository.findByIdWithEducation(1L)).thenReturn(Optional.of(user));
        when(educationRepository.findByUserIdAndDegree(1L, "BSc")).thenReturn(Optional.empty());
        when(educationRepository.save(any(Education.class))).thenReturn(education);

        Education savedEducation = educationService.save(education);
        assertEquals("BSc", savedEducation.getDegree());
    }

    @Test
    void testSaveEducation_InvalidUser() {
        // Scenario 2: Trying to save education with invalid user role
        User user = new User();
        user.setId(1L);
        user.setRole(Role.builder().roleType(RoleType.PROFESSIONAL).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Education education = new Education();

        when(userRepository.findByIdWithEducation(1L)).thenReturn(Optional.of(user));

        assertThrows(OperationException.class, () -> educationService.save(education));
    }
    @Test
    void testSaveEducation_WithEmptyDegree() {
        // Scenario 3: Saving education with empty degree
        User user = new User();
        user.setId(1L);
        user.setRole(Role.builder().roleType(RoleType.PROFESSIONAL).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Education education = new Education();
        education.setInstitution("University");
        education.setGraduationDate(LocalDate.parse("2023-01-01"));

        when(userRepository.findByIdWithEducation(1L)).thenReturn(Optional.of(user));

        assertThrows(OperationException.class, () -> educationService.save(education));
    }

    @Test
    void testSaveEducation_WithEmptyInstitution() {
        // Scenario 4: Saving education with empty institution
        User user = new User();
        user.setId(1L);
        user.setRole(Role.builder().roleType(RoleType.PROFESSIONAL).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Education education = new Education();
        education.setDegree("BSc");
        education.setGraduationDate(LocalDate.parse("2023-01-01"));

        when(userRepository.findByIdWithEducation(1L)).thenReturn(Optional.of(user));

        assertThrows(OperationException.class, () -> educationService.save(education));
    }

    @Test
    void testSaveEducation_WithNullGraduationDate() {
        // Scenario 5: Saving education with null graduation date
        User user = new User();
        user.setId(1L);
        user.setRole(Role.builder().roleType(RoleType.PROFESSIONAL).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Education education = new Education();
        education.setDegree("BSc");
        education.setInstitution("University");

        when(userRepository.findByIdWithEducation(1L)).thenReturn(Optional.of(user));

        assertThrows(OperationException.class, () -> educationService.save(education));
    }

    @Test
    void testSaveEducation_DuplicateDegree() {
        // Scenario 6: Saving duplicate education
        User user = new User();
        user.setId(1L);
        user.setRole(Role.builder().roleType(RoleType.PROFESSIONAL).build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Education education = new Education();
        education.setDegree("BSc");
        education.setInstitution("University");
        education.setGraduationDate(LocalDate.parse("2023-01-01"));

        when(userRepository.findByIdWithEducation(1L)).thenReturn(Optional.of(user));
        when(educationRepository.findByUserIdAndDegree(1L, "BSc")).thenReturn(Optional.of(new Education()));

        assertThrows(OperationException.class, () -> educationService.save(education));
    }

    @Test
    void testFindAllEducations_Success() {
        // Scenario 7: Finding all educations successfully
        List<Education> educations = new ArrayList<>();
        educations.add(new Education());

        when(educationRepository.findAll()).thenReturn(educations);

        List<Education> result = educationService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllEducations_EmptyList() {
        // Scenario 8: No educations found
        when(educationRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(OperationException.class, () -> educationService.findAll());
    }

}

