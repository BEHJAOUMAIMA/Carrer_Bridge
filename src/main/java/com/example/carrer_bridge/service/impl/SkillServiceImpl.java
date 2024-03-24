package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Skill;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.SkillRepository;
import com.example.carrer_bridge.repository.UserRepository;
import com.example.carrer_bridge.service.SkillService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Skill save(Skill skill) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authenticatedUser = userRepository.findByIdWithSkills(authenticatedUser.getId())
                .orElseThrow(() -> new OperationException("User not found"));

        if (skill.getName() == null || skill.getName().isEmpty()) {
            throw new OperationException("Skill name must not be empty");
        }
        if (skill.getProficiencyLevel() == null) {
            throw new OperationException("Proficiency level must not be null");
        }
        if (skill.getProficiencyLevel() < 0) {
            throw new OperationException("Proficiency level must be non-negative");
        }
        if (authenticatedUser.getRole().getRoleType() != RoleType.PROFESSIONAL) {
            throw new OperationException("Only users with role 'Professional' can save skills");
        }

        Optional<Skill> existingSkill = skillRepository.findByUserIdAndName(authenticatedUser.getId(), skill.getName());
        if (existingSkill.isPresent()) {
            throw new OperationException("Skill already exists for this user");
        }

        skill.setUser(authenticatedUser);
        return skillRepository.save(skill);
    }


    @Override
    public List<Skill> findAll() {
        List<Skill> skills = skillRepository.findAll();
        if (skills.isEmpty()) {
            throw new OperationException("No skills found!");
        } else {
            return skills;
        }
    }

    @Override
    public Optional<Skill> findById(Long id) {
        Optional<Skill> skill = skillRepository.findById(id);
        if (skill.isEmpty()) {
            throw new OperationException("No Skill found for this ID!");
        } else {
            return skill;
        }
    }

    @Override
    public Skill update(Skill skillUpdated, Long id) {
        Optional<Skill> existingSkillOptional = skillRepository.findById(id);
        if (existingSkillOptional.isEmpty()) {
            throw new OperationException("Skill not found with ID: " + id);
        }
        Skill existingSkill = existingSkillOptional.get();

        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (authenticatedUser.getRole().getRoleType() != RoleType.PROFESSIONAL) {
            throw new OperationException("Only users with role 'Professional' can update skills");
        }

        if (skillUpdated.getName() == null || skillUpdated.getName().isEmpty()) {
            throw new OperationException("Skill name must not be empty");
        }
        if (skillUpdated.getProficiencyLevel() == null) {
            throw new OperationException("Proficiency level must not be null");
        }
        if (skillUpdated.getProficiencyLevel() < 0) {
            throw new OperationException("Proficiency level must be non-negative");
        }
        Optional<Skill> existingSkillWithSameName = skillRepository.findByUserIdAndName(authenticatedUser.getId(), skillUpdated.getName());
        if (existingSkillWithSameName.isPresent() && !existingSkillWithSameName.get().getId().equals(existingSkill.getId())) {
            throw new OperationException("Another skill with the same name already exists");
        }

        if (!existingSkill.getUser().getId().equals(authenticatedUser.getId())) {
            throw new OperationException("You are not authorized to update this skill");
        }

        existingSkill.setName(skillUpdated.getName());
        existingSkill.setProficiencyLevel(skillUpdated.getProficiencyLevel());

        return skillRepository.save(existingSkill);
    }

    @Override
    public void delete(Long id) {
        Skill existingSkill = skillRepository.findById(id)
                .orElseThrow(() -> new OperationException("Skill not found with id: " + id));
        skillRepository.delete(existingSkill);
    }

    @Override
    public List<Skill> findSkillsByName(String name) {
        List<Skill> skillsByName = skillRepository.findByName(name);
        if (skillsByName.isEmpty()) {
            throw new OperationException("No skills found by this name !");
        } else {
            return skillsByName;
        }
    }

    @Override
    public List<Skill> findSkillsByUser() {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Skill> skillsByUser = skillRepository.findByUser(authenticatedUser);
        if (skillsByUser.isEmpty()) {
            throw new OperationException("No skills found for this user !");
        } else {
            return skillsByUser;
        }
    }
}
