package com.example.carrer_bridge.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class Recruiter extends User{

    private String industry;
    private String responsibility;

    @OneToMany(mappedBy = "recruiter")
    private List<JobOpportunity> jobOpportunities;
}
