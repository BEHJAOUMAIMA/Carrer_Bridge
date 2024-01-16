package com.example.carrer_bridge.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Firstname cannot be empty !")
    @NotNull(message = "Firstname must not be Null")
    @Size(min = 3, max = 50, message = "Firstname must be between 3 and 50 characters long")
    private String firstName;

    @NotBlank(message = "Lastname cannot be empty !")
    @NotNull(message = "Lastname must not be Null")
    @Size(min = 3, max = 50, message = "Lastname must be between 3 and 50 characters long")
    private String lastName;

    @NotBlank(message = "Email Address cannot be empty !")
    @NotNull(message = "Email Address must not be Null")
    @Email(message = "Email address is not valid")
    private String email;

    @NotBlank(message = "Password cannot be empty !")
    @NotNull(message = "Password must not be Null")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters long")
    private String password;

    @NotBlank(message = "Role type cannot be empty !")
    @NotNull(message = "Role type must not be Null")
    private String roleType;

}
