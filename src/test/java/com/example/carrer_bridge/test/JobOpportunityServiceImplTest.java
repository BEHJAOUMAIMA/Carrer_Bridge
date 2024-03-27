package com.example.carrer_bridge.test;

import com.example.carrer_bridge.domain.entities.*;
import com.example.carrer_bridge.domain.enums.ContractType;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.domain.enums.WorkingMode;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.JobOpportunityRepository;
import com.example.carrer_bridge.service.impl.JobOpportunityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class JobOpportunityServiceImplTest {
    @Mock
    private JobOpportunityRepository jobOpportunityRepository;
    @InjectMocks
    private JobOpportunityServiceImpl jobOpportunityService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveJobOpportunity_Success() {
        // Scénario 1: Création réussie d'une opportunité d'emploi
        User recruiter = new User();
        recruiter.setId(1L);
        recruiter.setRole(Role.builder().roleType(RoleType.RECRUITER).build());

        Authentication authentication = new UsernamePasswordAuthenticationToken(recruiter, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JobOpportunity jobOpportunity = new JobOpportunity();
        jobOpportunity.setTitle("Software Engineer");
        jobOpportunity.setDescription("Exciting opportunity for software engineers.");
        jobOpportunity.setCompany(new Company());

        when(jobOpportunityRepository.existsByTitleAndUser_IdAndCompany_Id("Software Engineer", 1L, null)).thenReturn(false);
        when(jobOpportunityRepository.save(any(JobOpportunity.class))).thenReturn(jobOpportunity);

        JobOpportunity savedOpportunity = jobOpportunityService.save(jobOpportunity);
        assertEquals("Software Engineer", savedOpportunity.getTitle());
    }

    @Test
    void testSaveJobOpportunity_DuplicateTitle() {
        // Scénario 2: Tentative de création d'une opportunité d'emploi avec un titre en double
        User recruiter = new User();
        recruiter.setId(1L);
        recruiter.setRole(Role.builder().roleType(RoleType.RECRUITER).build());

        Authentication authentication = new UsernamePasswordAuthenticationToken(recruiter, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JobOpportunity jobOpportunity = new JobOpportunity();
        jobOpportunity.setTitle("Software Engineer");
        jobOpportunity.setDescription("Exciting opportunity for software engineers.");
        jobOpportunity.setCompany(new Company());

        when(jobOpportunityRepository.existsByTitleAndUser_IdAndCompany_Id("Software Engineer", 1L, null)).thenReturn(true);

        assertThrows(OperationException.class, () -> jobOpportunityService.save(jobOpportunity));
    }

    @Test
    void testFindAllJobOpportunities_Success() {
        // Scénario 3: Récupération réussie de toutes les opportunités d'emploi
        List<JobOpportunity> jobOpportunities = new ArrayList<>();
        jobOpportunities.add(new JobOpportunity());

        when(jobOpportunityRepository.findAll()).thenReturn(jobOpportunities);

        List<JobOpportunity> result = jobOpportunityService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testFindJobOpportunityById_Success() {
        // Scénario 4: Récupération réussie d'une opportunité d'emploi par ID
        JobOpportunity jobOpportunity = new JobOpportunity();
        jobOpportunity.setId(1L);

        when(jobOpportunityRepository.findById(1L)).thenReturn(Optional.of(jobOpportunity));

        Optional<JobOpportunity> result = jobOpportunityService.findById(1L);
        assertEquals(jobOpportunity.getId(), result.get().getId());
    }

    @Test
    void testUpdateJobOpportunity_Success() {
        // Scenario 5: Successful update of a job opportunity
        User recruiter = new User();
        recruiter.setId(1L);
        recruiter.setRole(Role.builder().roleType(RoleType.RECRUITER).build());

        Authentication authentication = new UsernamePasswordAuthenticationToken(recruiter, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JobOpportunity existingOpportunity = new JobOpportunity();
        existingOpportunity.setId(1L);
        existingOpportunity.setTitle("Software Engineer");
        existingOpportunity.setDescription("Exciting opportunity for software engineers.");
        existingOpportunity.setCompany(new Company());
        existingOpportunity.setUser(recruiter);

        JobOpportunity updatedOpportunity = new JobOpportunity();
        updatedOpportunity.setTitle("Senior Software Engineer");
        updatedOpportunity.setDescription("Exciting opportunity for senior software engineers.");

        when(jobOpportunityRepository.findById(1L)).thenReturn(Optional.of(existingOpportunity));
        when(jobOpportunityRepository.existsByTitleAndUser_IdAndCompany_Id("Senior Software Engineer", 1L, null)).thenReturn(false);
        when(jobOpportunityRepository.save(any(JobOpportunity.class))).thenReturn(updatedOpportunity);

        JobOpportunity result = jobOpportunityService.update(updatedOpportunity, 1L);
        assertEquals("Senior Software Engineer", result.getTitle());
    }

    @Test
    void testUpdateJobOpportunity_DuplicateTitle() {
        // Scenario 6: Attempt to update a job opportunity with a duplicate title
        User recruiter = new User();
        recruiter.setId(1L);
        recruiter.setRole(Role.builder().roleType(RoleType.RECRUITER).build());

        Authentication authentication = new UsernamePasswordAuthenticationToken(recruiter, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JobOpportunity existingOpportunity = new JobOpportunity();
        existingOpportunity.setId(1L);
        existingOpportunity.setTitle("Software Engineer");
        existingOpportunity.setDescription("Exciting opportunity for software engineers.");
        existingOpportunity.setCompany(new Company());
        existingOpportunity.setUser(recruiter);

        JobOpportunity updatedOpportunity = new JobOpportunity();
        updatedOpportunity.setTitle("Senior Software Engineer");
        updatedOpportunity.setDescription("Exciting opportunity for senior software engineers.");

        when(jobOpportunityRepository.findById(1L)).thenReturn(Optional.of(existingOpportunity));
        when(jobOpportunityRepository.existsByTitleAndUser_IdAndCompany_Id("Senior Software Engineer", 1L, null)).thenReturn(true);

        assertThrows(OperationException.class, () -> jobOpportunityService.update(updatedOpportunity, 1L));
    }

    @Test
    void testDeleteJobOpportunity_Success() {
        // Scenario 7: Successful deletion of a job opportunity
        when(jobOpportunityRepository.findById(1L)).thenReturn(Optional.of(new JobOpportunity()));

        jobOpportunityService.delete(1L);
        // No assertion required, as long as no exception is thrown, deletion is considered successful
    }

    @Test
    void testDeleteJobOpportunity_NonExistingId() {
        // Scenario 8: Attempt to delete a job opportunity with a non-existing ID
        when(jobOpportunityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> jobOpportunityService.delete(1L));
    }


    @Test
    void testFindByCriteria_Success() {
        // Scenario 10: Successful retrieval of job opportunities by criteria
        String title = "Software Engineer";
        String description = "Exciting opportunity";
        String requiredSkills = "Java, Spring";
        LocalDateTime expirationDate = LocalDateTime.of(2023, 12, 31, 23, 59);
        ContractType contractType = ContractType.CDI;
        WorkingMode workingMode = WorkingMode.OFFICE;
        City city = new City();
        ExperienceDegree experienceDegree = new ExperienceDegree();
        TrainingDegree trainingDegree = new TrainingDegree();
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 0, 0);

        JobOpportunity exampleOpportunity = new JobOpportunity();
        exampleOpportunity.setTitle(title);
        exampleOpportunity.setDescription(description);
        exampleOpportunity.setRequiredSkills(requiredSkills);
        exampleOpportunity.setExpirationDate(expirationDate);
        exampleOpportunity.setContractType(contractType);
        exampleOpportunity.setWorkingMode(workingMode);
        exampleOpportunity.setCity(city);
        exampleOpportunity.setExperienceDegree(experienceDegree);
        exampleOpportunity.setTrainingDegree(trainingDegree);
        exampleOpportunity.setCreatedAt(createdAt);

        List<JobOpportunity> expectedOpportunities = new ArrayList<>();
        expectedOpportunities.add(new JobOpportunity());

        when(jobOpportunityRepository.findAll((Example<JobOpportunity>) any())).thenReturn(expectedOpportunities);

        List<JobOpportunity> result = jobOpportunityService.findByCriteria(title, description, requiredSkills,
                expirationDate, contractType, workingMode, city, experienceDegree, trainingDegree, createdAt);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByCriteria_EmptyResult() {
        // Scenario 11: Retrieval of job opportunities by criteria returns an empty result
        String title = "Software Engineer";
        String description = "Exciting opportunity";
        String requiredSkills = "Java, Spring";
        LocalDateTime expirationDate = LocalDateTime.of(2023, 12, 31, 23, 59);
        ContractType contractType = ContractType.CDD;
        WorkingMode workingMode = WorkingMode.HYBRID;
        City city = new City();
        ExperienceDegree experienceDegree = new ExperienceDegree();
        TrainingDegree trainingDegree = new TrainingDegree();
        LocalDateTime createdAt = LocalDateTime.of(2023, 1, 1, 0, 0);

        JobOpportunity exampleOpportunity = new JobOpportunity();
        exampleOpportunity.setTitle(title);
        exampleOpportunity.setDescription(description);
        exampleOpportunity.setRequiredSkills(requiredSkills);
        exampleOpportunity.setExpirationDate(expirationDate);
        exampleOpportunity.setContractType(contractType);
        exampleOpportunity.setWorkingMode(workingMode);
        exampleOpportunity.setCity(city);
        exampleOpportunity.setExperienceDegree(experienceDegree);
        exampleOpportunity.setTrainingDegree(trainingDegree);
        exampleOpportunity.setCreatedAt(createdAt);

        when(jobOpportunityRepository.findAll((Example<JobOpportunity>) any())).thenReturn(new ArrayList<>());

        List<JobOpportunity> result = jobOpportunityService.findByCriteria(title, description, requiredSkills,
                expirationDate, contractType, workingMode, city, experienceDegree, trainingDegree, createdAt);
        assertEquals(0, result.size());
    }

    @Test
    void testFindByCriteria_NullValues() {
        // Scenario 12: Retrieval of job opportunities by criteria with null values
        when(jobOpportunityRepository.findAll((Example<JobOpportunity>) any())).thenReturn(new ArrayList<>());

        List<JobOpportunity> result = jobOpportunityService.findByCriteria(null, null, null,
                null, null, null, null, null, null, null);
        assertEquals(0, result.size());
    }



}
