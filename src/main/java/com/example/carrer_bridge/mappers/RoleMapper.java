package com.example.carrer_bridge.mappers;

import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.dto.response.RoleResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponseDto toResponseDto(Role role);

    @Mapping(target = "roleType", source = "roleType", qualifiedByName = "toRoleType")
    Role toEntity(Role roleRequestDto);

    @Named("toRoleType")
    default RoleType toRoleType(String roleTypeString) {
        return RoleType.valueOf(roleTypeString);
    }

}
