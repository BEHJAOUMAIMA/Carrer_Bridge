package com.example.carrer_bridge.mappers;


import com.example.carrer_bridge.domain.entities.TrainingDegree;
import com.example.carrer_bridge.dto.request.TrainingDegreeRequestDto;
import com.example.carrer_bridge.dto.response.TrainingDegreeResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingDegreeMapper {
    TrainingDegreeResponseDto toResponseDto(TrainingDegree trainingDegree);
    TrainingDegree fromRequestDto(TrainingDegreeRequestDto trainingDegreeRequestDto);

}
