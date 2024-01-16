package com.example.carrer_bridge.service;

import com.example.carrer_bridge.domain.entities.Permission;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PermissionService {
    Permission save(Permission permission);
    List<Permission> findAll();
    Optional<Permission> findById(Long id);
    Permission update(Permission permissionUpdated, Long id);
    void delete(Long id);
}
