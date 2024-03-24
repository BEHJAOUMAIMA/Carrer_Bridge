package com.example.carrer_bridge.mappers;

import com.example.carrer_bridge.domain.entities.Skill;
import com.example.carrer_bridge.dto.request.UserSkillsRequest;
import com.example.carrer_bridge.dto.response.UserSkillsResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserSkillsMapper {

    UserSkillsResponse toResponseDto(Skill skill);
    Skill fromRequestDto(UserSkillsRequest userSkillsRequest);

}
