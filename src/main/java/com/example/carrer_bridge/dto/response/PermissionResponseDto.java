package com.example.carrer_bridge.dto.response;

import com.example.carrer_bridge.domain.enums.PermissionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionResponseDto {

    private Long id;

    private PermissionType permissionType;

}
