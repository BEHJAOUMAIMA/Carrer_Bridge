package com.example.carrer_bridge.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationRequestDto {

    @NotBlank(message = "Full name must not be Blank !")
    @NotNull(message = "Full name must not be null !")
    private String fullName;

    @Email(message = "Email Format invalid !")
    @NotBlank(message = "Full name must not be Blank !")
    @NotNull(message = "Full name must not be null !")
    private String email;

    private String company;

    private String number;

    @NotBlank(message = "Subject must not be Blank !")
    @NotNull(message = "Subject must not be null !")
    private String subject;

    @NotBlank(message = "Message must not be Blank !")
    @NotNull(message = "Message must not be null !")
    private String message;

}
