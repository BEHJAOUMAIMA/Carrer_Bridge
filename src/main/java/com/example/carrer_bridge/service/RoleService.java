package com.example.carrer_bridge.service;

import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.enums.RoleType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RoleService {

    Role save(Role role);
    List<Role> findAll();
    Optional<Role> findById(Long id);
    Role update(Role roleUpdated, Long id);
    void delete(Long id);
    Optional<Role> findByName(RoleType roleType);
}
