package com.example.carrer_bridge.mappers;


import com.example.carrer_bridge.domain.entities.Permission;
import com.example.carrer_bridge.domain.enums.PermissionType;
import com.example.carrer_bridge.dto.request.PermissionRequestDto;
import com.example.carrer_bridge.dto.response.PermissionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponseDto toResponseDto(Permission permission);
    Permission fromRequestDto(PermissionRequestDto permissionRequestDto);

    @Mapping(target = "permissionType", source = "permissionType", qualifiedByName = "toPermissionType")
    Permission toEntity(Permission permissionRequestDto);

    @Named("toPermissionType")
    default PermissionType toPermissionType(String permissionTypeString) {
        return PermissionType.valueOf(permissionTypeString);
    }
}
