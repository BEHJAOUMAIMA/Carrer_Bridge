package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
}
