package com.example.carrer_bridge.service;


import com.example.carrer_bridge.domain.entities.UserProfile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserProfileService {
    UserProfile save(UserProfile userProfile);
    List<UserProfile> findAll();
    Optional<UserProfile> findById(Long id);
    UserProfile update(UserProfile userUpdated, Long id);
    void delete(Long id);
}
