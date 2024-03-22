package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.ExperienceDegree;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.ExperienceDegreeRepository;
import com.example.carrer_bridge.service.ExperienceDegreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExperienceDegreeServiceImpl implements ExperienceDegreeService {

    private final ExperienceDegreeRepository experienceDegreeRepository;
    @Override
    public ExperienceDegree save(ExperienceDegree experienceDegree) {
        Optional<ExperienceDegree> existingExperienceDegree= experienceDegreeRepository.findByDegree(experienceDegree.getDegree());
        if (existingExperienceDegree.isPresent()){
            throw new OperationException("Experience Degree already exists with type: " + experienceDegree.getDegree());
        }
        return experienceDegreeRepository.save(experienceDegree);    }

    @Override
    public List<ExperienceDegree> findAll() {
        List<ExperienceDegree> experienceDegrees = experienceDegreeRepository.findAll();
        if (experienceDegrees.isEmpty()) {
            throw new OperationException("No experience Degrees found!");
        } else {
            return experienceDegrees;
        }
    }

    @Override
    public Optional<ExperienceDegree> findById(Long id) {
        Optional<ExperienceDegree> experienceDegree = experienceDegreeRepository.findById(id);
        if (experienceDegree.isEmpty()) {
            throw new OperationException("No experience Degree found for this ID!");
        } else {
            return experienceDegree;
        }
    }

    @Override
    public ExperienceDegree update(ExperienceDegree experienceDegreeUpdated, Long id) {
        ExperienceDegree existingExperienceDegree = experienceDegreeRepository.findById(id)
                .orElseThrow(() -> new OperationException("Experience Degree not found with id: " + id));
        existingExperienceDegree.setDegree(experienceDegreeUpdated.getDegree());
        return experienceDegreeRepository.save(existingExperienceDegree);
    }

    @Override
    public void delete(Long id) {
        ExperienceDegree existingExperienceDegree = experienceDegreeRepository.findById(id)
                .orElseThrow(() -> new OperationException("Experience Degree not found with id: " + id));
        experienceDegreeRepository.delete(existingExperienceDegree);
    }
}
