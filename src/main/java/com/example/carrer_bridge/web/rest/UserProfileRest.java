package com.example.carrer_bridge.web.rest;

import com.example.carrer_bridge.domain.entities.*;
import com.example.carrer_bridge.dto.request.UserProfileRequestDto;
import com.example.carrer_bridge.dto.response.UserProfileResponseDto;
import com.example.carrer_bridge.mappers.UserProfileMapper;
import com.example.carrer_bridge.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/myProfile")
public class UserProfileRest {

    private final UserProfileService userProfileService;
    private final UserProfileMapper userProfileMapper;

    @GetMapping("/view")
    public ResponseEntity<UserProfileResponseDto> viewProfile() {
        UserProfile userProfile = userProfileService.viewProfile();
        UserProfileResponseDto responseDto = userProfileMapper.toResponseDto(userProfile);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/update")
    public ResponseEntity<UserProfileResponseDto> updateProfile(@RequestBody UserProfileRequestDto requestDto) {
        UserProfile userProfile = userProfileMapper.fromRequestDto(requestDto);
        User user = userProfile.getUser();
        UserProfile updatedProfile = userProfileService.updateProfile(userProfile, user);
        UserProfileResponseDto responseDto = userProfileMapper.toResponseDto(updatedProfile);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/update-password")
    public ResponseEntity<UserProfileResponseDto> updatePassword(@RequestBody UserProfileRequestDto requestDto) {
        UserProfile userProfile = userProfileService.updatePassword(requestDto.getCurrentPassword(), requestDto.getNewPassword(), requestDto.getConfirmedNewPassword());
        UserProfileResponseDto responseDto = userProfileMapper.toResponseDto(userProfile);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/upload-image")
    public ResponseEntity<UserProfileResponseDto> uploadProfileImage(@RequestParam("image") MultipartFile image) {
        UserProfile userProfile = userProfileService.uploadProfileImage(image);
        UserProfileResponseDto responseDto = userProfileMapper.toResponseDto(userProfile);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/skills")
    public ResponseEntity<Void> addOrUpdateSkills(@RequestBody List<Skill> userSkills) {
        userProfileService.addOrUpdateSkills(userSkills);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/experiences")
    public ResponseEntity<Void> addOrUpdateExperiences(@RequestBody List<Experience> userExperiences) {
        userProfileService.addOrUpdateExperiences(userExperiences);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/educations")
    public ResponseEntity<Void> addOrUpdateEducations(@RequestBody List<Education> userEducation) {
        userProfileService.addOrUpdateEducations(userEducation);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/skills/{skillId}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long skillId) {
        userProfileService.deleteSkill(skillId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/experiences/{experienceId}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long experienceId) {
        userProfileService.deleteExperience(experienceId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/educations/{educationId}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long educationId) {
        userProfileService.deleteEducation(educationId);
        return ResponseEntity.ok().build();
    }

}
