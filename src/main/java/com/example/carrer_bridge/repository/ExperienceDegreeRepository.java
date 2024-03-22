package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.ExperienceDegree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExperienceDegreeRepository extends JpaRepository<ExperienceDegree, Long> {

    Optional<ExperienceDegree> findByDegree(String degree);

}
