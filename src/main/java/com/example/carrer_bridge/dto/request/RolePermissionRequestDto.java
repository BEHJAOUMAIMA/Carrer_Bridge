package com.example.carrer_bridge.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionRequestDto {

    @NotNull(message ="Role cannot be null !" )
    private Long roleId;

    @NotNull(message = "Permission cannot be null !")
    private Long permissionId;
}
