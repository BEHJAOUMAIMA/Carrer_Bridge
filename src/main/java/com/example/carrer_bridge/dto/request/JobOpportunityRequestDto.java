package com.example.carrer_bridge.dto.request;

import com.example.carrer_bridge.domain.enums.ContractType;
import com.example.carrer_bridge.domain.enums.WorkingMode;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOpportunityRequestDto {

    @NotNull(message = "Recruiter ID cannot be null")
    private Long recruiterId;

    @NotNull(message = "Company ID cannot be null")
    private Long companyId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than or equal to 255 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Required skills are required")
    private String requiredSkills;

    @Future(message = "Expiration date must be in the future")
    @NotNull(message = "Expiration date cannot be null")
    private LocalDateTime expirationDate;

    @NotNull(message = "Contract type cannot be null")
    private ContractType contractType;

    @NotNull(message = "Working mode cannot be null")
    private WorkingMode workingMode;

    @NotNull(message = "City ID cannot be null")
    private Long cityId;

    @NotNull(message = "Experience degree ID cannot be null")
    private Long experienceDegreeId;

    @NotNull(message = "Training degree ID cannot be null")
    private Long trainingDegreeId;

}
