package com.example.carrer_bridge.service;

import com.example.carrer_bridge.domain.entities.RolePermission;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RolePermissionService {
    RolePermission save(RolePermission rolePermission);
    List<RolePermission> findAll();
    Optional<RolePermission> findById(Long id);
    RolePermission update(RolePermission rolePermissionUpdated, Long id);
    void delete(Long id);
}
