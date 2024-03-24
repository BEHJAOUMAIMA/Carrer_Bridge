package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.Education;
import com.example.carrer_bridge.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    Optional<Education> findByUserIdAndDegree(Long userId, String degree);
    List<Education> findByDegree(String degree);
    List<Education> findByUser(User user);
}
