package com.example.carrer_bridge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {

    private Long id;

    private String profileImage;
    private String bio;

    private String firstName;
    private String lastName;
    private String email;

    private String industry;
    private String responsibility;

    private List<UserSkillsResponse> skills;
    private List<UserExperienceResponse> experiences;
    private List<UserProfileResponseDto> trainings;

}
