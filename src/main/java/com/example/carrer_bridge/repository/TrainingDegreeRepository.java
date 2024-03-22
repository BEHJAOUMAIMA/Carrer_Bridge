package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.TrainingDegree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingDegreeRepository extends JpaRepository<TrainingDegree, Long> {
    Optional<TrainingDegree> findByDegree(String degree);
    boolean existsByDegree(String degree);

}
