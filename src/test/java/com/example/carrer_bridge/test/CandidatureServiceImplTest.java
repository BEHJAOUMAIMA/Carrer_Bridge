package com.example.carrer_bridge.test;

import com.example.carrer_bridge.domain.entities.Candidature;
import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.CandidatureRepository;

import com.example.carrer_bridge.service.impl.CandidatureServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CandidatureServiceImplTest {

    @Mock
    private CandidatureRepository candidatureRepository;

    @InjectMocks
    private CandidatureServiceImpl candidatureService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        candidatureService = new CandidatureServiceImpl(candidatureRepository);
    }

    @Test
    public void shouldReturnAllCandidaturesForAdministrator() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User authenticatedUser = mock(User.class);
        Role role = mock(Role.class);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);
        when(authenticatedUser.getRole()).thenReturn(role);
        when(role.getRoleType()).thenReturn(RoleType.ADMINISTRATOR);
        List<Candidature> expectedCandidatures = new ArrayList<>();
        when(candidatureRepository.findAll()).thenReturn(expectedCandidatures);

        // Act
        List<Candidature> actualCandidatures = candidatureService.findAll();

        // Assert
        assertEquals(expectedCandidatures, actualCandidatures);
    }

    @Test
    public void shouldReturnAllCandidaturesForRecruiter() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User authenticatedUser = mock(User.class);
        Role role = mock(Role.class);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);
        when(authenticatedUser.getRole()).thenReturn(role);
        when(role.getRoleType()).thenReturn(RoleType.RECRUITER);
        List<Candidature> expectedCandidatures = new ArrayList<>();
        when(candidatureRepository.findByJobOpportunityUser(authenticatedUser)).thenReturn(expectedCandidatures);

        // Act
        List<Candidature> actualCandidatures = candidatureService.findAll();

        // Assert
        assertEquals(expectedCandidatures, actualCandidatures);
    }

    @Test
    public void shouldReturnAllCandidaturesForProfessional() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User authenticatedUser = mock(User.class);
        Role role = mock(Role.class);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);
        when(authenticatedUser.getRole()).thenReturn(role);
        when(role.getRoleType()).thenReturn(RoleType.PROFESSIONAL);
        List<Candidature> expectedCandidatures = new ArrayList<>();
        when(candidatureRepository.findByUser(authenticatedUser)).thenReturn(expectedCandidatures);

        // Act
        List<Candidature> actualCandidatures = candidatureService.findAll();

        // Assert
        assertEquals(expectedCandidatures, actualCandidatures);
    }

    @Test
    public void shouldThrowExceptionIfCandidatureIdIsNull() {
        // Act and Assert
        assertThrows(OperationException.class, () -> candidatureService.findById(null));
    }

    @Test
    public void shouldThrowExceptionIfUserIdIsNull() {
        // Act and Assert
        assertThrows(OperationException.class, () -> candidatureService.findByUserId(null));
    }

    @Test
    public void shouldThrowExceptionIfJobOpportunityIdIsNull() {
        // Act and Assert
        assertThrows(OperationException.class, () -> candidatureService.findByJobOpportunityId(null));
    }
}
