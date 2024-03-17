package com.example.carrer_bridge.web.rest;

import com.example.carrer_bridge.domain.entities.Permission;
import com.example.carrer_bridge.dto.request.PermissionRequestDto;
import com.example.carrer_bridge.dto.response.PermissionResponseDto;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.PermissionMapper;
import com.example.carrer_bridge.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/permissions")
public class PermissionRest {

    private final PermissionService permissionService;
    private final PermissionMapper permissionMapper;

    @GetMapping
    public ResponseEntity<List<PermissionResponseDto>> getAllPermissions() {
        List<Permission> permissions = permissionService.findAll();
        List<PermissionResponseDto> permissionResponseDtos = permissions.stream().map(permissionMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(permissionResponseDtos);
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<?> getPermissionById(@PathVariable Long permissionId) {
        Permission permission = permissionService.findById(permissionId)
                .orElseThrow(() -> new OperationException("Permission not found with ID: " + permissionId));
        PermissionResponseDto permissionResponseDto = permissionMapper.toResponseDto(permission);
        return ResponseEntity.ok(permissionResponseDto);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage> addPermission(@Valid @RequestBody PermissionRequestDto permissionRequestDto) {
        Permission permission = permissionService.save(permissionMapper.fromRequestDto(permissionRequestDto));
        if (permission == null) {
            return ResponseMessage.badRequest("Permission not created");
        } else {
            return ResponseMessage.created("Permission created successfully", permission);
        }
    }

    @PutMapping("/update/{permissionId}")
    public ResponseEntity<ResponseMessage> updatePermission(@PathVariable Long permissionId, @Valid @RequestBody PermissionRequestDto permissionRequestDto) {
        Permission updatedPermission = permissionMapper.fromRequestDto(permissionRequestDto);
        Permission permission = permissionService.update(updatedPermission, permissionId);
        return ResponseEntity.ok(ResponseMessage.created("Permission updated successfully", permission).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deletePermission(@PathVariable Long id) {
        Optional<Permission> existingPermission = permissionService.findById(id);
        if (existingPermission.isEmpty()) {
            return ResponseMessage.notFound("Permission not found with ID: " + id);
        }
        permissionService.delete(id);
        return ResponseMessage.ok("Permission deleted successfully with ID: " + id, null);
    }
}
