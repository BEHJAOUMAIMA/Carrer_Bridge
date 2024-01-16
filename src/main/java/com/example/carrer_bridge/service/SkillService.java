package com.example.carrer_bridge.service;

import com.example.carrer_bridge.domain.entities.Skill;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SkillService {
    Skill save(Skill skill);
    List<Skill> findAll();
    Optional<Skill> findById(Long id);
    Skill update(Skill skillUpdated, Long id);
    void delete(Long id);
}
