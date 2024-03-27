package com.example.carrer_bridge.dto.response;

import com.example.carrer_bridge.domain.enums.CandidatureStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatureResponseDTO {

    private Long id;
    private UserResponseDto professional;
    private JobOpportunityResponseDto jobOpportunity;
    private LocalDateTime applicationDate;
    private CandidatureStatus status;

}
