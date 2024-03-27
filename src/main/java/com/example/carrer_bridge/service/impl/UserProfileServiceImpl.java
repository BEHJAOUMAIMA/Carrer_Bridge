package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.*;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.UserProfileRepository;
import com.example.carrer_bridge.repository.UserRepository;
import com.example.carrer_bridge.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService{

    @Value("${upload.dir.path}")
    private String uploadDirPath;

    private final UserProfileRepository userProfileRepository;
    private final ExperienceService experienceService;
    private final EducationService educationService;
    private final SkillService skillService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public UserProfile viewProfile() {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserProfile userProfile = userProfileRepository.findByUserId(authenticatedUser.getId())
                .orElseThrow(() -> new OperationException("UserProfile not found for the authenticated user."));

        userProfile.setUser(authenticatedUser);
        List<Skill> skills = authenticatedUser.getSkills();
        List<Experience> experiences = authenticatedUser.getExperiences();
        List<Education> educations = authenticatedUser.getEducation();

        userProfile.getUser().setFirstName(authenticatedUser.getFirstName());
        userProfile.getUser().setLastName(authenticatedUser.getLastName());
        userProfile.getUser().setEmail(authenticatedUser.getEmail());
        userProfile.getUser().setSkills(skills);
        userProfile.getUser().setExperiences(experiences);
        userProfile.getUser().setEducation(educations);
        userProfile.setBio(authenticatedUser.getUserProfile().getBio());
        userProfile.setProfileImage(authenticatedUser.getUserProfile().getProfileImage());

        return userProfile;
    }
    @Override
    public UserProfile updateProfile(UserProfile userProfile, User user) {

        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       if (authenticatedUser.getFirstName()==null || authenticatedUser.getFirstName().isEmpty()){
           throw new OperationException("User Firstname must not be null or empty !");
       }
       if (authenticatedUser.getLastName()==null || authenticatedUser.getLastName().isEmpty()){
           throw new OperationException("User Lastname must not be null or get !");
       }
       if (authenticatedUser.getEmail()==null || authenticatedUser.getEmail().isEmpty()){
           throw new OperationException("User Email must not be null or empty !");
       }

       authenticatedUser.setFirstName(user.getFirstName());
       authenticatedUser.setLastName(user.getLastName());
       authenticatedUser.setEmail(user.getEmail());
       userRepository.save(authenticatedUser);

       Optional<UserProfile> existingUserProfile = userProfileRepository.findByUserId(authenticatedUser.getId());
       if (existingUserProfile.isPresent()){
           throw new OperationException("UserProfile already exist ! you can update Your information's !");
       }
       return userProfileRepository.save(userProfile);

    }
    @Override
    public UserProfile updatePassword(String currentPassword, String newPassword, String confirmedNewPassword) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!passwordEncoder.matches(currentPassword, authenticatedUser.getPassword())) {
            throw new OperationException("Current password is incorrect.");
        }

        if (!newPassword.equals(confirmedNewPassword)) {
            throw new OperationException("New password and confirmed password do not match.");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);

        authenticatedUser.setPassword(encodedPassword);
        userRepository.save(authenticatedUser);

        return userProfileRepository.findByUserId(authenticatedUser.getId())
                .orElseThrow(() -> new OperationException("UserProfile not found for the authenticated user."));
    }
    @Override
    public UserProfile uploadProfileImage(MultipartFile image) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            String uploadDir = uploadDirPath + "/" + authenticatedUser.getId();
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath);
            UserProfile userProfile = authenticatedUser.getUserProfile();
            userProfile.setProfileImage(fileName);
            userProfileRepository.save(userProfile);
            return userProfile;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile image.", e);
        }
    }
    @Override
    public void addOrUpdateSkills(List<Skill> userSkills) {
        for (Skill skill : userSkills) {
            if (skill.getId() != null && skillService.findById(skill.getId()).isPresent()) {
                skillService.update(skill, skill.getId());
            } else {
                skillService.save(skill);
            }
        }
    }
    @Override
    public void addOrUpdateExperiences(List<Experience> userExperiences) {
        for (Experience experience : userExperiences) {
            if (experience.getId() != null && experienceService.findById(experience.getId()).isPresent()) {
                experienceService.update(experience, experience.getId());
            } else {
                experienceService.save(experience);
            }
        }
    }
    @Override
    public void addOrUpdateEducations(List<Education> userEducation) {
        for (Education education : userEducation) {
            if (education.getId() != null && educationService.findById(education.getId()).isPresent()) {
                educationService.update(education, education.getId());
            } else {
                educationService.save(education);
            }
        }
    }
    @Override
    public void addOrUpdateIndustryAndResponsibility(String industry, String responsibility) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authenticatedUser.setIndustry(industry);
        authenticatedUser.setResponsibility(responsibility);
        userService.save(authenticatedUser);
    }

    @Override
    public void deleteSkill(Long skillId) {
        skillService.delete(skillId);
    }

    @Override
    public void deleteExperience(Long experienceId) {
        experienceService.delete(experienceId);
    }

    @Override
    public void deleteEducation(Long educationId) {
        educationService.delete(educationId);
    }
}
