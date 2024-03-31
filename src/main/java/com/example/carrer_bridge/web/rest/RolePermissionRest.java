package com.example.carrer_bridge.web.rest;

import com.example.carrer_bridge.domain.entities.Permission;
import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.entities.RolePermission;
import com.example.carrer_bridge.dto.request.RolePermissionRequestDto;
import com.example.carrer_bridge.dto.response.RolePermissionResponseDto;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.RolePermissionMapper;
import com.example.carrer_bridge.service.PermissionService;
import com.example.carrer_bridge.service.RolePermissionService;
import com.example.carrer_bridge.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/role-permissions")
public class RolePermissionRest {

    private final RolePermissionService rolePermissionService;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final RolePermissionMapper rolePermissionMapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('VIEW_ROLE_PERMISSION')")
    public ResponseEntity<List<RolePermissionResponseDto>> getAllRolePermissions() {
        List<RolePermission> rolePermissions = rolePermissionService.findAll();
        List<RolePermissionResponseDto> rolePermissionResponseDtos = rolePermissions.stream()
                .map(rolePermissionMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rolePermissionResponseDtos);
    }


    @GetMapping("/{rolePermissionId}")
    @PreAuthorize("hasAnyAuthority('VIEW_ROLE_PERMISSION')")
    public ResponseEntity<?> getRolePermissionById(@PathVariable Long rolePermissionId) {
        RolePermission rolePermission = rolePermissionService.findById(rolePermissionId)
                .orElseThrow(() -> new OperationException("RolePermission not found with ID: " + rolePermissionId));
        RolePermissionResponseDto rolePermissionResponseDto = rolePermissionMapper.toResponseDto(rolePermission);
        return ResponseEntity.ok(rolePermissionResponseDto);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('CREATE_ROLE_PERMISSION')")
    public ResponseEntity<ResponseMessage> addRolePermission(@Valid @RequestBody RolePermissionRequestDto requestDto) {
        Role role = roleService.findById(requestDto.getRoleId())
                .orElseThrow(() -> new OperationException("Role not found with ID: " + requestDto.getRoleId()));

        Permission permission = permissionService.findById(requestDto.getPermissionId())
                .orElseThrow(() -> new OperationException("Permission not found with ID: " + requestDto.getPermissionId()));

        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);

        RolePermission savedRolePermission = rolePermissionService.save(rolePermission);

        return ResponseEntity.ok(ResponseMessage.created("Role permission created successfully", savedRolePermission).getBody());
    }
    @PutMapping("/update/{rolePermissionId}")
    @PreAuthorize("hasAnyAuthority('UPDATE_ROLE_PERMISSION')")
    public ResponseEntity<ResponseMessage> updateRolePermission(@PathVariable Long rolePermissionId, @Valid @RequestBody RolePermissionRequestDto rolePermissionRequestDto) {
        RolePermission updatedRolePermission = rolePermissionMapper.fromRequestDto(rolePermissionRequestDto);
        RolePermission rolePermission = rolePermissionService.update(updatedRolePermission, rolePermissionId);
        return ResponseEntity.ok(ResponseMessage.created("Role Permission updated successfully", rolePermission).getBody());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('DELETE_ROLE_PERMISSION')")
    public ResponseEntity<ResponseMessage> deleteRolePermission(@PathVariable Long id) {
        Optional<RolePermission> existingRolePermission = rolePermissionService.findById(id);
        if (existingRolePermission.isEmpty()) {
            return ResponseMessage.notFound("RolePermission not found with ID: " + id);
        }
        rolePermissionService.delete(id);
        return ResponseMessage.ok("RolePermission deleted successfully with ID: " + id, null);
    }

}
