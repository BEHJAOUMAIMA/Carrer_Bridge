package com.example.carrer_bridge.dto.response;

import com.example.carrer_bridge.domain.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDto {

    private Long id;

    private RoleType roleType;

}
