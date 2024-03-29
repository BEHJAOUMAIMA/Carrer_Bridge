package com.example.carrer_bridge.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Integer duration;
    private LocalDateTime startDate;
    private String location;
    private Long maxPlaces;

    @ManyToOne
    private User user;

    @ManyToMany(mappedBy = "trainings" , fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> professionals;

}
