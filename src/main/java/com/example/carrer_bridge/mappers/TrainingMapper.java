package com.example.carrer_bridge.mappers;


import com.example.carrer_bridge.domain.entities.Training;
import com.example.carrer_bridge.dto.request.TrainingRequestDto;
import com.example.carrer_bridge.dto.response.TrainingResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
    TrainingResponseDto toResponseDto(Training training);
    Training fromRequestDto(TrainingRequestDto trainingRequestDto);

}
