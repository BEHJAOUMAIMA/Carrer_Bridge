package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.Communication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Long> {
    long countByFullNameAndEmail(String fullName, String email);
    Optional<Communication> findBySubject(String subject);

}
