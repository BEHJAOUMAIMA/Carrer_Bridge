package com.example.carrer_bridge.domain.entities;

import com.example.carrer_bridge.domain.enums.CandidatureStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Candidature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professional_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_opportunity_id")
    private JobOpportunity jobOpportunity;

    private LocalDateTime applicationDate;

    @Enumerated(EnumType.STRING)
    private CandidatureStatus status;
}
