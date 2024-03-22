package com.example.carrer_bridge.mappers;


import com.example.carrer_bridge.domain.entities.ExperienceDegree;
import com.example.carrer_bridge.dto.request.ExperienceDegreeRequestDto;
import com.example.carrer_bridge.dto.response.ExperienceDegreeResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExperienceDegreeMapper {
    ExperienceDegreeResponseDto toResponseDto(ExperienceDegree experienceDegree);
    ExperienceDegree fromRequestDto(ExperienceDegreeRequestDto experienceDegreeRequestDto);

}
