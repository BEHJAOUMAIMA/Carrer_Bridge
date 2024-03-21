package com.example.carrer_bridge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationResponseDto {

    private Long id;
    private String fullName;
    private String email;
    private String company;
    private String number;
    private String subject;
    private String message;

}
