package com.example.carrer_bridge.repository;

import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
