package com.example.carrer_bridge.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class Professional extends User{
    @OneToMany(mappedBy = "professional")
    private List<Skill> skills;

    @OneToMany(mappedBy = "professional")
    private List<Experience> experiences;

    @OneToMany(mappedBy = "professional")
    private List<Education> education;

    @ManyToMany
    @JoinTable(
            name = "professional_trainings",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id"))
    private List<Training> trainings;

}
