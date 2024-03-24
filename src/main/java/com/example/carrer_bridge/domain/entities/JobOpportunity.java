package com.example.carrer_bridge.domain.entities;

import com.example.carrer_bridge.domain.enums.ContractType;
import com.example.carrer_bridge.domain.enums.WorkingMode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recruiter_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    private String title;
    private String description;
    private String requiredSkills;
    private LocalDateTime expirationDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @Enumerated(EnumType.STRING)
    private WorkingMode workingMode;

    @ManyToOne
    private City city;

    @ManyToOne
    private ExperienceDegree experienceDegree;

    @ManyToOne
    private TrainingDegree trainingDegree;

    @OneToMany(mappedBy = "jobOpportunity", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Candidature> candidatures;

}
