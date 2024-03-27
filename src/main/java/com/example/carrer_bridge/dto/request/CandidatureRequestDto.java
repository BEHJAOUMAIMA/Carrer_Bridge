package com.example.carrer_bridge.dto.request;

import com.example.carrer_bridge.domain.enums.CandidatureStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatureRequestDto {

    @NotNull(message = "Job Opportunity ID cannot be null")
    private Long jobOpportunityId;

    private LocalDateTime applicationDate = LocalDateTime.now();

    private CandidatureStatus status = CandidatureStatus.PENDING;

}
