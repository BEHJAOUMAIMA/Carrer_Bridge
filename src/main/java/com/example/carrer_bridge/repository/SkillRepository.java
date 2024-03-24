package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.Skill;
import com.example.carrer_bridge.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByUserIdAndName(Long userId, String name);
    List<Skill> findByName(String name);
    List<Skill> findByUser(User user);
}
