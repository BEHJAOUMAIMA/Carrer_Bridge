package com.example.carrer_bridge.dto.request;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequestDto {

    @NotBlank(message = "profile Image must not be blank")
    @NotEmpty(message = "profile Image is required !")
    private String profileImage;

    @NotBlank(message = "User Bio must not be blank")
    @NotEmpty(message = "User Bio is required !")
    private String bio;

    @NotBlank(message = "First Name must not be blank")
    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @NotEmpty(message = "Last name is required")
    private String lastName;

    @Email
    @NotEmpty(message = "Email is required")
    private String email;


    private String currentPassword;
    private String newPassword;
    private String confirmedNewPassword;

    private List<UserSkillsRequest> skills;
    private List<UserExperienceRequest> experiences;
    private List<UserEducationRequest> trainings;

    private String industry;
    private String responsibility;
}
