package com.example.carrer_bridge.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExperienceDegreeRequestDto {

    @NotBlank(message = "Experience Degree is required !")
    private String degree;

}
