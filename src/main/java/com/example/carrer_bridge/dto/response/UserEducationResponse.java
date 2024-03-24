package com.example.carrer_bridge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEducationResponse {

    private Long id;
    private String degree;
    private String institution;
    private LocalDate graduationDate;
    private UserResponseDto user;

}
