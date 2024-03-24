package com.example.carrer_bridge.mappers;


import com.example.carrer_bridge.domain.entities.Experience;
import com.example.carrer_bridge.dto.request.UserExperienceRequest;
import com.example.carrer_bridge.dto.response.UserExperienceResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserExperienceMapper {
    UserExperienceResponse toResponseDto(Experience experience);
    Experience fromRequestDto(UserExperienceRequest userExperienceRequest);
}
