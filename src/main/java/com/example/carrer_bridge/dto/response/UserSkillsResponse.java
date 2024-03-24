package com.example.carrer_bridge.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSkillsResponse {

    private Long id;
    private String name;
    private Integer proficiencyLevel;
    private UserResponseDto user;

}
