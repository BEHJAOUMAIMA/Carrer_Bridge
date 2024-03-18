package com.example.carrer_bridge.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @NotNull(message = "firstname is required")
    private String firstName;

    @NotBlank
    @NotNull(message = "lastname is required")
    private String lastName;

    @Email(message = "email format is not valid")
    @NotNull
    private String email;

    @NotBlank
    private String roleType;

    @NotNull
    private String password;

    @NotNull
    private String confirmedPassword;

}
