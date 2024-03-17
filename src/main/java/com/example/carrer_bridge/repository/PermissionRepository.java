package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.Permission;
import com.example.carrer_bridge.domain.enums.PermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionType(PermissionType permissionType);

}
