package com.example.carrer_bridge.web.rest;

import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.dto.request.UserRequestDto;
import com.example.carrer_bridge.dto.response.UserResponseDto;
import com.example.carrer_bridge.handler.response.ResponseMessage;
import com.example.carrer_bridge.mappers.UserMapper;
import com.example.carrer_bridge.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRest {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserResponseDto> userResponseDTOs = users.stream()
                .map(userMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(userResponseDTOs);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        Optional<User> user = userService.findById(userId);

        if (user.isEmpty()) {
            return ResponseMessage.notFound("User not found with ID: " + userId);
        }

        UserResponseDto userResponseDto = userMapper.toResponseDto(user.get());
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage> addUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        User user = userService.save(userMapper.fromRequestDto(userRequestDto));
        if(user == null) {
            return ResponseMessage.badRequest("User not created");
        }else {
            return ResponseMessage.created("User created successfully", user);
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ResponseMessage> updateUser(@PathVariable Long userId, @Valid @RequestBody UserRequestDto userRequestDto) {

        User updatedUser = userMapper.fromRequestDto(userRequestDto);
        User user = userService.update(updatedUser, userId);

        return ResponseEntity.ok(ResponseMessage.created("User updated successfully", user).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userService.findById(id);

        if (existingUser.isEmpty()) {
            return ResponseMessage.notFound("User not found with ID: " + id);
        }

        userService.delete(id);

        return ResponseMessage.ok("User deleted successfully with ID: " + id, null);
    }

}
