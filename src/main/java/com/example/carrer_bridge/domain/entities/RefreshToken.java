package com.example.carrer_bridge.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="user_id", referencedColumnName = "id")
    private User user;

    @Column( nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    private boolean revoked;

}

