package com.example.carrer_bridge.mappers;


import com.example.carrer_bridge.domain.entities.City;
import com.example.carrer_bridge.dto.request.CityRequestDto;
import com.example.carrer_bridge.dto.response.CityResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityResponseDto toResponseDto(City city);
    City fromRequestDto(CityRequestDto cityRequestDto);

}
