package com.example.carrer_bridge.mappers;

import com.example.carrer_bridge.domain.entities.RolePermission;
import com.example.carrer_bridge.dto.request.RolePermissionRequestDto;
import com.example.carrer_bridge.dto.response.RolePermissionResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {
    RolePermissionResponseDto toResponseDto(RolePermission rolePermission);
    RolePermission fromRequestDto(RolePermissionRequestDto rolePermissionRequestDto);

}
