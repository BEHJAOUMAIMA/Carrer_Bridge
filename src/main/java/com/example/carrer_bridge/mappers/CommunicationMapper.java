package com.example.carrer_bridge.mappers;


import com.example.carrer_bridge.domain.entities.Communication;
import com.example.carrer_bridge.dto.request.CommunicationRequestDto;
import com.example.carrer_bridge.dto.response.CommunicationResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommunicationMapper {
    CommunicationResponseDto toResponseDto(Communication communication);
    Communication fromRequestDto(CommunicationRequestDto communicationRequestDto);
}
