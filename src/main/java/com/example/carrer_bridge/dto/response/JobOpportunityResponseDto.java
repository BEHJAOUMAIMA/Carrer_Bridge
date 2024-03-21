package com.example.carrer_bridge.dto.response;

import com.example.carrer_bridge.domain.entities.*;
import com.example.carrer_bridge.domain.enums.ContractType;
import com.example.carrer_bridge.domain.enums.WorkingMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOpportunityResponseDto {

    private Long id;
    private User recruiter;
    private Company company;
    private String title;
    private String description;
    private String requiredSkills;
    private LocalDateTime expirationDate;
    private ContractType contractType;
    private WorkingMode workingMode;
    private City city;
    private ExperienceDegree experienceDegree;
    private TrainingDegree trainingDegree;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
