package com.example.carrer_bridge.mappers;

import com.example.carrer_bridge.domain.entities.Candidature;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.dto.request.CandidatureRequestDto;
import com.example.carrer_bridge.dto.response.CandidatureResponseDTO;
import com.example.carrer_bridge.dto.response.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidatureMapper {
    CandidatureResponseDTO toResponseDto(Candidature candidature);
    Candidature fromRequestDto(CandidatureRequestDto candidatureRequestDto);
    UserResponseDto mapUserToUserResponseDto(User user);
}
