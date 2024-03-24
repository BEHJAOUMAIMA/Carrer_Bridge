package com.example.carrer_bridge.mappers;

import com.example.carrer_bridge.domain.entities.UserProfile;
import com.example.carrer_bridge.dto.request.UserProfileRequestDto;
import com.example.carrer_bridge.dto.response.UserProfileResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileResponseDto toResponseDto(UserProfile userProfile);
    UserProfile fromRequestDto(UserProfileRequestDto userProfileRequestDto);

}
