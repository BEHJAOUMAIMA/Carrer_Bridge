package com.example.carrer_bridge.mappers;


import com.example.carrer_bridge.domain.entities.Education;
import com.example.carrer_bridge.dto.request.UserEducationRequest;
import com.example.carrer_bridge.dto.response.UserEducationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEducationMapper {
    UserEducationResponse toResponseDto(Education education);
    Education fromRequestDto(UserEducationRequest userEducationRequest);
    
}
