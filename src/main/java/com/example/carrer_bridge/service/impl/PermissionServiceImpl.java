package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Permission;
import com.example.carrer_bridge.domain.enums.PermissionType;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.PermissionRepository;
import com.example.carrer_bridge.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    @Override
    public Permission save(Permission permission) {
        Optional<Permission> existingPermission = permissionRepository.findByPermissionType(permission.getPermissionType());
        if (existingPermission.isPresent()){
            throw new OperationException("Permission already exists with type: " + permission.getPermissionType());
        }
        return permissionRepository.save(permission);
    }

    @Override
    public List<Permission> findAll() {
        List<Permission> permissions = permissionRepository.findAll();
        if (permissions.isEmpty()) {
            throw new OperationException("No permissions found!");
        } else {
            return permissions;
        }
    }

    @Override
    public Optional<Permission> findById(Long id) {
        Optional<Permission> permission = permissionRepository.findById(id);
        if (permission.isEmpty()) {
            throw new OperationException("No permission found for this ID!");
        } else {
            return permission;
        }
    }

    @Override
    public Permission update(Permission permissionUpdated, Long id) {
        Permission existingPermission = permissionRepository.findByPermissionType(permissionUpdated.getPermissionType())
                .orElseThrow(() -> new OperationException("Permission not found with type: " + permissionUpdated.getPermissionType()));
        if (!existingPermission.getId().equals(id)) {
            throw new OperationException("Permission already exists with type: " + permissionUpdated.getPermissionType());
        }

        existingPermission.setPermissionType(permissionUpdated.getPermissionType());
        return permissionRepository.save(existingPermission);
    }


    @Override
    public void delete(Long id) {
        Permission existingPermission = permissionRepository.findById(id)
                .orElseThrow(() -> new OperationException("Permission not found with id: " + id));
        permissionRepository.delete(existingPermission);
    }

    @Override
    public Optional<Permission> findByName(PermissionType permissionType) {
        return permissionRepository.findByPermissionType(permissionType);
    }
}
