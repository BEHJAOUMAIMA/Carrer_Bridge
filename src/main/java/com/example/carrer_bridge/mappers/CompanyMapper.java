package com.example.carrer_bridge.mappers;

import com.example.carrer_bridge.domain.entities.Company;
import com.example.carrer_bridge.dto.request.CompanyRequestDto;
import com.example.carrer_bridge.dto.response.CompanyResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyResponseDto toResponseDto(Company company);
    Company fromRequestDto(CompanyRequestDto companyResponseDto);

}
