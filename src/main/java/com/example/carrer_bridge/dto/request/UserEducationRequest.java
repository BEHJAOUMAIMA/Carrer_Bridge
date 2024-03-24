package com.example.carrer_bridge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEducationRequest {

    @NotBlank
    @NotNull
    private String degree;

    @NotNull
    @NotBlank
    private String institution;

    @NotNull
    private LocalDate graduationDate;
}
