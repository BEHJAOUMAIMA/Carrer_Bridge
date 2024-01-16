package com.example.carrer_bridge.dto.request;

import com.example.carrer_bridge.domain.enums.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequestDto {

    @NotNull(message = "Role Type of User must not been null !")
    private RoleType roleType;

}
