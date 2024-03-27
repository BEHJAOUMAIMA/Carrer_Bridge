package com.example.carrer_bridge.test;

import com.example.carrer_bridge.domain.entities.Communication;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.CommunicationRepository;
import com.example.carrer_bridge.service.impl.CommunicationServiceImpl;

import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CommunicationServiceImplTest {

    private static final Long MAX_COMMUNICATIONS_ALLOWED = 3L;
    CommunicationRepository mockCommunicationRepository = mock(CommunicationRepository.class);

    @Test
    public void test_saveNewCommunicationWithValidData() {

        CommunicationServiceImpl communicationService = new CommunicationServiceImpl(mockCommunicationRepository);
        Communication communication = new Communication();
        communication.setFullName("John Doe");
        communication.setEmail("johndoe@example.com");
        communication.setCompany("Example Company");
        communication.setNumber("1234567890");
        communication.setSubject("Test Subject");
        communication.setMessage("Test Message");

        Communication savedCommunication = communicationService.save(communication);

        assertEquals(communication, savedCommunication);
    }
    @Test
    public void test_findAllCommunicationsWhenSomeExist() {

        CommunicationServiceImpl communicationService = new CommunicationServiceImpl(mockCommunicationRepository);
        List<Communication> expectedCommunications = new ArrayList<>();
        Communication communication1 = new Communication();
        communication1.setId(1L);
        communication1.setFullName("John Doe");
        communication1.setEmail("johndoe@example.com");
        communication1.setCompany("Example Company");
        communication1.setNumber("1234567890");
        communication1.setSubject("Test Subject 1");
        communication1.setMessage("Test Message 1");
        expectedCommunications.add(communication1);
        Communication communication2 = new Communication();
        communication2.setId(2L);
        communication2.setFullName("Jane Smith");
        communication2.setEmail("janesmith@example.com");
        communication2.setCompany("Example Company");
        communication2.setNumber("0987654321");
        communication2.setSubject("Test Subject 2");
        communication2.setMessage("Test Message 2");
        expectedCommunications.add(communication2);
        when(mockCommunicationRepository.findAll()).thenReturn(expectedCommunications);

        List<Communication> actualCommunications = communicationService.findAll();
        assertEquals(expectedCommunications, actualCommunications);
    }
    @Test
    public void test_findCommunicationByIdWhenItExists() {

        CommunicationServiceImpl communicationService = new CommunicationServiceImpl(mockCommunicationRepository);
        Long id = 1L;
        Communication expectedCommunication = new Communication();
        expectedCommunication.setId(id);
        expectedCommunication.setFullName("John Doe");
        expectedCommunication.setEmail("johndoe@example.com");
        expectedCommunication.setCompany("Example Company");
        expectedCommunication.setNumber("1234567890");
        expectedCommunication.setSubject("Test Subject");
        expectedCommunication.setMessage("Test Message");
        when(mockCommunicationRepository.findById(id)).thenReturn(Optional.of(expectedCommunication));


        Optional<Communication> actualCommunication = communicationService.findById(id);


        assertTrue(actualCommunication.isPresent());
        assertEquals(expectedCommunication, actualCommunication.get());
    }
    @Test
    public void test_updateCommunicationWithValidData() {

        CommunicationServiceImpl communicationService = new CommunicationServiceImpl(mockCommunicationRepository);
        Long id = 1L;
        Communication existingCommunication = new Communication();
        existingCommunication.setId(id);
        existingCommunication.setFullName("John Doe");
        existingCommunication.setEmail("johndoe@example.com");
        existingCommunication.setCompany("Example Company");
        existingCommunication.setNumber("1234567890");
        existingCommunication.setSubject("Test Subject");
        existingCommunication.setMessage("Test Message");
        Communication updatedCommunication = new Communication();
        updatedCommunication.setId(id);
        updatedCommunication.setFullName("Jane Smith");
        updatedCommunication.setEmail("janesmith@example.com");
        updatedCommunication.setCompany("Updated Company");
        updatedCommunication.setNumber("0987654321");
        updatedCommunication.setSubject("Updated Subject");
        updatedCommunication.setMessage("Updated Message");
        when(mockCommunicationRepository.findById(id)).thenReturn(Optional.of(existingCommunication));
        when(mockCommunicationRepository.save(existingCommunication)).thenReturn(updatedCommunication);

        Communication actualCommunication = communicationService.update(updatedCommunication, id);

        assertEquals(updatedCommunication, actualCommunication);
    }
    @Test
    public void test_deleteCommunicationByIdWhenItExists() {

        CommunicationServiceImpl communicationService = new CommunicationServiceImpl(mockCommunicationRepository);
        Long id = 1L;
        Communication existingCommunication = new Communication();
        existingCommunication.setId(id);
        existingCommunication.setFullName("John Doe");
        existingCommunication.setEmail("johndoe@example.com");
        existingCommunication.setCompany("Example Company");
        existingCommunication.setNumber("1234567890");
        existingCommunication.setSubject("Test Subject");
        existingCommunication.setMessage("Test Message");
        when(mockCommunicationRepository.findById(id)).thenReturn(Optional.of(existingCommunication));

        communicationService.delete(id);

        verify(mockCommunicationRepository).delete(existingCommunication);
    }
    @Test
    public void test_processCommunicationBySubjectWhenItExists() {

        CommunicationServiceImpl communicationService = new CommunicationServiceImpl(mockCommunicationRepository);
        String subject = "Test Subject";
        Communication expectedCommunication = new Communication();
        expectedCommunication.setId(1L);
        expectedCommunication.setFullName("John Doe");
        expectedCommunication.setEmail("johndoe@example.com");
        expectedCommunication.setCompany("Example Company");
        expectedCommunication.setNumber("1234567890");
        expectedCommunication.setSubject(subject);
        expectedCommunication.setMessage("Test Message");
        when(mockCommunicationRepository.findBySubject(subject)).thenReturn(Optional.of(expectedCommunication));

        String actualContent = communicationService.processCommunicationBySubject(subject);

        String expectedContent = "Content of the communication with the subject \"" + subject + "\" : " + expectedCommunication.getMessage();
        assertEquals(expectedContent, actualContent);
    }
    @Test
    public void test_saveNewCommunicationWithSameFullNameAndEmailAsExistingCommunication() {
        CommunicationServiceImpl communicationService = new CommunicationServiceImpl(mockCommunicationRepository);
        Communication existingCommunication = new Communication();
        existingCommunication.setFullName("John Doe");
        existingCommunication.setEmail("johndoe@example.com");
        when(mockCommunicationRepository.countByFullNameAndEmail(existingCommunication.getFullName(), existingCommunication.getEmail())).thenReturn(MAX_COMMUNICATIONS_ALLOWED);
        assertThrows(OperationException.class, () -> communicationService.save(existingCommunication));
    }
    @Test
    public void test_findAllCommunicationsWhenNoneExist() {

        CommunicationServiceImpl communicationService = new CommunicationServiceImpl(mockCommunicationRepository);
        when(mockCommunicationRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(OperationException.class, communicationService::findAll);
    }
    @Test
    public void test_findCommunicationByIdWhenItDoesNotExist() {

        CommunicationServiceImpl communicationService = new CommunicationServiceImpl(mockCommunicationRepository);
        Long id = 1L;
        when(mockCommunicationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> communicationService.findById(id));
    }
    @Test
    public void test_updateCommunicationWithNonexistentId() {

        CommunicationServiceImpl communicationService = new CommunicationServiceImpl(mockCommunicationRepository);
        Long id = 1L;
        Communication updatedCommunication = new Communication();
        updatedCommunication.setId(id);
        updatedCommunication.setFullName("Jane Smith");
        updatedCommunication.setEmail("janesmith@example.com");
        updatedCommunication.setCompany("Updated Company");
        updatedCommunication.setNumber("0987654321");
        updatedCommunication.setSubject("Updated Subject");
        updatedCommunication.setMessage("Updated Message");
        when(mockCommunicationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> communicationService.update(updatedCommunication, id));
    }
    @Test
    public void test_deleteCommunicationByIdWhenItDoesNotExist() {

        CommunicationServiceImpl communicationService = new CommunicationServiceImpl(mockCommunicationRepository);
        Long id = 1L;
        when(mockCommunicationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(OperationException.class, () -> communicationService.delete(id));
    }
    @Test
    public void test_processCommunicationByNonexistentSubject() {

        CommunicationServiceImpl communicationService = new CommunicationServiceImpl(mockCommunicationRepository);
        String subject = "Nonexistent Subject";
        when(mockCommunicationRepository.findBySubject(subject)).thenReturn(Optional.empty());

        String actualContent = communicationService.processCommunicationBySubject(subject);
        String expectedContent = "No communication found with subject: " + subject;
        assertEquals(expectedContent, actualContent);
    }

}