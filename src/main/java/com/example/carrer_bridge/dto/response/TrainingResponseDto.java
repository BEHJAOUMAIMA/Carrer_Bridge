package com.example.carrer_bridge.dto.response;

import com.example.carrer_bridge.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingResponseDto {

    private Long id;

    private String title;

    private String description;

    private Integer duration;

    private LocalDateTime startDate;

    private String location;

    private Long maxPlaces;

    private UserResponseDto user;

}
