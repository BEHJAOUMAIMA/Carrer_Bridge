package com.example.carrer_bridge.dto.response;

import com.example.carrer_bridge.domain.entities.Permission;
import com.example.carrer_bridge.domain.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionResponseDto {

    private Long id;
    private Role role;
    private Permission permission;

}

