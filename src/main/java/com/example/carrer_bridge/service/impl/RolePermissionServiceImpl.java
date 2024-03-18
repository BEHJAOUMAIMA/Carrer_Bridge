package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Permission;
import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.entities.RolePermission;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.RolePermissionRepository;
import com.example.carrer_bridge.service.PermissionService;
import com.example.carrer_bridge.service.RolePermissionService;
import com.example.carrer_bridge.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RolePermissionServiceImpl implements RolePermissionService {

    private  final RolePermissionRepository rolePermissionRepository;
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Override
    public RolePermission save(RolePermission rolePermission) {
        if (rolePermission == null || rolePermission.getRole() == null || rolePermission.getPermission() == null) {
            throw new OperationException("RolePermission, role, or permission cannot be null.");
        }

        Role role = rolePermission.getRole();
        Permission permission = rolePermission.getPermission();

        Long roleId = role.getId();
        Long permissionId = permission.getId();

        if (roleId == null || permissionId == null) {
            throw new OperationException("Role ID or permission ID cannot be null.");
        }

        Role existingRole = roleService.findById(roleId)
                .orElseThrow(() -> new OperationException("Role not found with ID: " + roleId));

        Permission existingPermission = permissionService.findById(permissionId)
                .orElseThrow(() -> new OperationException("Permission not found with ID: " + permissionId));

        List<RolePermission> rolePermissions = existingRole.getRolePermissions();
        if (rolePermissions != null) {
            boolean hasPermission = rolePermissions.stream()
                    .anyMatch(rp -> rp.getPermission().equals(existingPermission));

            if (hasPermission) {
                throw new OperationException("The role already has the permission associated.");
            }
        }

        return rolePermissionRepository.save(rolePermission);
    }

    @Override
    public List<RolePermission> findAll() {
        List<RolePermission> rolePermissions = rolePermissionRepository.findAll();
        if (rolePermissions.isEmpty()) {
            throw new OperationException("No Roles & Permissions found!");
        } else {
            return rolePermissions;
        }
    }

    @Override
    public Optional<RolePermission> findById(Long id) {
        Optional<RolePermission> rolePermission = rolePermissionRepository.findById(id);
        if (rolePermission.isEmpty()) {
            throw new OperationException("No role & permissions found for this ID!");
        } else {
            return rolePermission;
        }
    }

    @Override
    public RolePermission update(RolePermission rolePermissionUpdated, Long id) {
        RolePermission existingRolePermission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new OperationException("RolePermission not found with id: " + id));

        Role existingRole = existingRolePermission.getRole();
        Permission existingPermission = existingRolePermission.getPermission();

        boolean hasPermission = existingRole.getRolePermissions()
                .stream()
                .anyMatch(rp -> rp.getPermission().equals(existingPermission));

        if (hasPermission) {
            throw new OperationException("The role already has the permission associated.");
        }

        existingRolePermission.setRole(rolePermissionUpdated.getRole());
        existingRolePermission.setPermission(rolePermissionUpdated.getPermission());

        return rolePermissionRepository.save(existingRolePermission);
    }

    @Override
    public void delete(Long id) {
        RolePermission existingRolePermission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new OperationException("RolePermission not found with id: " + id));
        rolePermissionRepository.delete(existingRolePermission);
    }
}
