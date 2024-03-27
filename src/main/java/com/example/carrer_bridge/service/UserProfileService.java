package com.example.carrer_bridge.service;


import com.example.carrer_bridge.domain.entities.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public interface UserProfileService {
    UserProfile viewProfile();

    UserProfile updateProfile(UserProfile userProfile, User user);
    UserProfile updatePassword(String currentPassword, String newPassword, String confirmedNewPassword);
    UserProfile uploadProfileImage(MultipartFile image);
    void addOrUpdateSkills(List<Skill> userSkills);
    void addOrUpdateExperiences(List<Experience> userExperiences);
    void addOrUpdateEducations(List<Education> userEducation);
    void addOrUpdateIndustryAndResponsibility(String industry, String responsibility);
    void deleteSkill(Long skillId);
    void deleteExperience(Long experienceId);
    void deleteEducation(Long educationId);

}
