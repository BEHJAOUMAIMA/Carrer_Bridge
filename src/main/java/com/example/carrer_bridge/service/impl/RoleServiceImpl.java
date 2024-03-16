package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.repository.RoleRepository;
import com.example.carrer_bridge.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role save(Role role) {
        return null;
    }

    @Override
    public List<Role> findAll() {
        return null;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Role update(Role roleUpdated, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Optional<Role> findByName(RoleType roleType) {
        return roleRepository.findByRoleType(roleType);
    }
}
