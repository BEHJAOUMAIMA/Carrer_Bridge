package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.entities.UserProfile;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.mappers.RoleMapper;
import com.example.carrer_bridge.repository.RoleRepository;
import com.example.carrer_bridge.repository.UserRepository;
import com.example.carrer_bridge.service.RoleService;
import com.example.carrer_bridge.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User save(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new OperationException("Email already exists");
        }

        RoleType roleType = user.getRole().getRoleType();
        Role role = roleRepository.findByRoleType(roleType)
                .orElseThrow(() -> new OperationException("Role not found or invalid"));

        user.setRole(role);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .build();

        user.setUserProfile(userProfile);

        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User update(User userUpdated, Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new OperationException("User not found"));

        existingUser.setFirstName(userUpdated.getFirstName());
        existingUser.setLastName(userUpdated.getLastName());

        if (!existingUser.getRole().getRoleType().equals(userUpdated.getRole().getRoleType())) {

            RoleType roleType = userUpdated.getRole().getRoleType();
            Role role = roleRepository.findByRoleType(roleType)
                    .orElseThrow(() -> new OperationException("New role not found or invalid"));
            existingUser.setRole(role);
        }

        if (!existingUser.getEmail().equals(userUpdated.getEmail()) &&
                userRepository.existsByEmail(userUpdated.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        existingUser.setEmail(userUpdated.getEmail());


        if (userUpdated.getPassword() != null && !userUpdated.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userUpdated.getPassword());
            existingUser.setPassword(encodedPassword);
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
