package com.example.carrer_bridge.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrainingDegreeRequestDto {

    @NotBlank(message = "Training Degree is required !")
    private String degree;

}
