package com.example.carrer_bridge.mappers;

import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.dto.request.UserRequestDto;
import com.example.carrer_bridge.dto.response.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toResponseDto(User user);
    User fromRequestDto(UserRequestDto userRequestDto);

}
