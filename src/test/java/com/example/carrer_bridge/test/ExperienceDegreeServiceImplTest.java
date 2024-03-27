package com.example.carrer_bridge.test;

import com.example.carrer_bridge.domain.entities.ExperienceDegree;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.ExperienceDegreeRepository;
import com.example.carrer_bridge.service.impl.ExperienceDegreeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExperienceDegreeServiceImplTest {

    @Mock
    private ExperienceDegreeRepository experienceDegreeRepository;

    @InjectMocks
    private ExperienceDegreeServiceImpl experienceDegreeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveExperienceDegree_Success() {
        // Scenario 1: Saving new experience degree successfully
        ExperienceDegree experienceDegree = new ExperienceDegree(1L, "Bachelor");

        when(experienceDegreeRepository.findByDegree("Bachelor")).thenReturn(Optional.empty());
        when(experienceDegreeRepository.save(any(ExperienceDegree.class))).thenReturn(experienceDegree);

        ExperienceDegree savedExperienceDegree = experienceDegreeService.save(experienceDegree);
        assertEquals("Bachelor", savedExperienceDegree.getDegree());
    }

    @Test
    void testSaveExperienceDegree_ExistingDegree() {
        ExperienceDegree existingExperienceDegree = new ExperienceDegree(2L,"Bachelor");

        when(experienceDegreeRepository.findByDegree("Bachelor")).thenReturn(Optional.of(existingExperienceDegree));

        assertThrows(OperationException.class, () -> experienceDegreeService.save(existingExperienceDegree));
    }

    @Test
    void testFindAllExperienceDegrees_Success() {
        // Scenario 3: Finding all experience degrees successfully
        List<ExperienceDegree> experienceDegrees = new ArrayList<>();
        experienceDegrees.add(new ExperienceDegree(1L,"Bachelor"));

        when(experienceDegreeRepository.findAll()).thenReturn(experienceDegrees);

        List<ExperienceDegree> result = experienceDegreeService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllExperienceDegrees_EmptyList() {
        // Scenario 4: No experience degrees found
        when(experienceDegreeRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(OperationException.class, () -> experienceDegreeService.findAll());
    }

    @Test
    void testFindExperienceDegreeById_Success() {
        // Scenario 5: Finding experience degree by ID successfully
        Long id = 1L;
        ExperienceDegree experienceDegree = new ExperienceDegree(1L,"Bachelor");

        when(experienceDegreeRepository.findById(id)).thenReturn(Optional.of(experienceDegree));

        Optional<ExperienceDegree> result = experienceDegreeService.findById(id);
        assertTrue(result.isPresent());
    }

    @Test
    void testFindExperienceDegreeById_NotFound() {
        // Scenario 6: No experience degree found by ID
        Long id = 1L;

        when(experienceDegreeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> experienceDegreeService.findById(id));
    }

    @Test
    void testUpdateExperienceDegree_Success() {
        // Scenario 7: Updating experience degree successfully
        Long id = 1L;
        ExperienceDegree existingExperienceDegree = new ExperienceDegree(2L,"Bachelor");
        ExperienceDegree updatedExperienceDegree = new ExperienceDegree(2L,"Master");

        when(experienceDegreeRepository.findById(id)).thenReturn(Optional.of(existingExperienceDegree));
        when(experienceDegreeRepository.existsByDegree(updatedExperienceDegree.getDegree())).thenReturn(false);
        when(experienceDegreeRepository.save(any(ExperienceDegree.class))).thenReturn(updatedExperienceDegree);

        ExperienceDegree result = experienceDegreeService.update(updatedExperienceDegree, id);
        assertEquals("Master", result.getDegree());
    }

    @Test
    void testUpdateExperienceDegree_ExistingDegree() {
        // Scenario 8: Trying to update experience degree with existing type
        Long id = 1L;
        ExperienceDegree existingExperienceDegree = new ExperienceDegree(2L,"Bachelor");
        ExperienceDegree updatedExperienceDegree = new ExperienceDegree(3L,"Bachelor");

        when(experienceDegreeRepository.findById(id)).thenReturn(Optional.of(existingExperienceDegree));
        when(experienceDegreeRepository.existsByDegree(updatedExperienceDegree.getDegree())).thenReturn(true);

        assertThrows(OperationException.class, () -> experienceDegreeService.update(updatedExperienceDegree, id));
    }

    @Test
    void testDeleteExperienceDegree_Success() {
        // Scenario 9: Deleting experience degree successfully
        Long id = 1L;
        ExperienceDegree existingExperienceDegree = new ExperienceDegree(1L,"Bachelor");

        when(experienceDegreeRepository.findById(id)).thenReturn(Optional.of(existingExperienceDegree));

        experienceDegreeService.delete(id);
        verify(experienceDegreeRepository, times(1)).delete(existingExperienceDegree);
    }

    @Test
    void testDeleteExperienceDegree_NotFound() {
        // Scenario 10: Trying to delete non-existing experience degree
        Long id = 1L;

        when(experienceDegreeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> experienceDegreeService.delete(id));
    }
}
