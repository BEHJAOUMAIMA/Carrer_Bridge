package com.example.carrer_bridge.domain.entities;

import com.example.carrer_bridge.domain.enums.ContractType;
import com.example.carrer_bridge.domain.enums.WorkingMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JobOpportunity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String title;
    private String description;
    private String requiredSkills;
    private LocalDateTime expirationDate;
    private ContractType contractType;
    private WorkingMode workingMode;
    @ManyToOne
    private City city;
    @ManyToOne
    private ExperienceDegree experienceDegree;
    @ManyToOne
    private TrainingDegree trainingDegree;



    @OneToMany(mappedBy = "jobOpportunity")
    private List<Candidature> candidatures;
}
