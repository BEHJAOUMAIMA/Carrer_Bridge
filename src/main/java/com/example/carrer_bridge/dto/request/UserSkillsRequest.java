package com.example.carrer_bridge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSkillsRequest {

    @NotBlank
    @NotNull
    private String name;

    @NotNull
    private Integer proficiencyLevel;
}
