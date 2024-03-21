package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.RoleRepository;
import com.example.carrer_bridge.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Component
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role save(Role role) {
        Optional<Role> existingRole = roleRepository.findByRoleType(role.getRoleType());
        if (existingRole.isPresent()){
            throw new OperationException("Role already exists with type: " + role.getRoleType());
        }
        return roleRepository.save(role);
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            throw new OperationException("No roles found!");
        } else {
            return roles;
        }
    }

    @Override
    public Optional<Role> findById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            throw new OperationException("No role found for this ID!");
        } else {
            return role;
        }
    }

    @Override
    public Role update(Role roleUpdated, Long id) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new OperationException("Role not found with id: " + id));

        existingRole.setRoleType(roleUpdated.getRoleType());
        return roleRepository.save(existingRole);
    }

    @Override
    public void delete(Long id) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new OperationException("Role not found with id: " + id));
        roleRepository.delete(existingRole);
    }

    @Override
    public Optional<Role> findByName(RoleType roleType) {
        return roleRepository.findByRoleType(roleType);
    }
}
