package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(User authenticatedUser);
    Optional<UserProfile> findByUserId(Long userId);
}
