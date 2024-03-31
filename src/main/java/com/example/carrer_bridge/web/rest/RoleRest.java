package com.example.carrer_bridge.web.rest;


import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.dto.request.RoleRequestDto;
import com.example.carrer_bridge.dto.response.RoleResponseDto;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.RoleMapper;
import com.example.carrer_bridge.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleRest {
    private final RoleService roleService;
    private final RoleMapper roleMapper;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('VIEW_ROLE')")
    public ResponseEntity<List<RoleResponseDto>> getAllRoles() {
        List<Role> roles = roleService.findAll();
        List<RoleResponseDto> roleResponseDTOs = roles.stream().map(roleMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(roleResponseDTOs);
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasAnyAuthority('VIEW_ROLE')")
    public ResponseEntity<?> getRoleById(@PathVariable Long roleId) {
        Role role = roleService.findById(roleId)
                .orElseThrow(() -> new OperationException("Role not found with ID: " + roleId));
        RoleResponseDto roleResponseDto = roleMapper.toResponseDto(role);
        return ResponseEntity.ok(roleResponseDto);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('CREATE_ROLE')")
    public ResponseEntity<ResponseMessage> addRole(@Valid @RequestBody RoleRequestDto roleRequestDto) {
        Role role = roleService.save(roleMapper.fromRequestDto(roleRequestDto));
        if (role == null) {
            return ResponseMessage.badRequest("Role not created");
        } else {
            return ResponseMessage.created("Role created successfully", role);
        }
    }

    @PutMapping("/update/{roleId}")
    @PreAuthorize("hasAnyAuthority('UPDATE_ROLE')")
    public ResponseEntity<ResponseMessage> updateRole(@PathVariable Long roleId, @Valid @RequestBody RoleRequestDto roleRequestDto) {
        Role updatedRole = roleMapper.fromRequestDto(roleRequestDto);
        Role role = roleService.update(updatedRole, roleId);
        return ResponseEntity.ok(ResponseMessage.created("Role updated successfully", role).getBody());
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('DELETE_ROLE')")
    public ResponseEntity<ResponseMessage> deleteRole(@PathVariable Long id) {
        Optional<Role> existingRole = roleService.findById(id);
        if (existingRole.isEmpty()) {
            return ResponseMessage.notFound("Role not found with ID: " + id);
        }
        roleService.delete(id);
        return ResponseMessage.ok("Role deleted successfully with ID: " + id, null);
    }


}
