package com.example.carrer_bridge.mappers;


import com.example.carrer_bridge.domain.entities.JobOpportunity;
import com.example.carrer_bridge.dto.request.JobOpportunityRequestDto;
import com.example.carrer_bridge.dto.response.JobOpportunityResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobOpportunityMapper {
    JobOpportunityResponseDto toResponseDto(JobOpportunity jobOpportunity);
    JobOpportunity fromRequestDto(JobOpportunityRequestDto jobOpportunityRequestDto);

}
