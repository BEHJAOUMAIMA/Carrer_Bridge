package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.Experience;
import com.example.carrer_bridge.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    Optional<Experience> findByUserIdAndTitle(Long userId, String title);
    List<Experience> findByTitle(String title);
    List<Experience> findByUser(User user);
}
