package com.example.carrer_bridge.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingRequestDto {

    @NotBlank(message = "Training Title must not be blank")
    @NotNull(message = "Training Title must not be null")
    private String title;

    @NotBlank(message = "Training Description must not be blank")
    @NotNull(message = "Training Description must not be null")
    private String description;

    @NotNull(message = "Training duration must not be null")
    private Integer duration;

    @NotNull(message = "Training start Date must not be null")
    @FutureOrPresent(message = "Training start Date must be in the present or future")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @NotBlank(message = "Training Location must not be blank")
    @NotNull(message = "Training Location must not be null")
    private String location;

    @NotNull(message = "Training Max places must not be null")
    private Long maxPlaces;

}
