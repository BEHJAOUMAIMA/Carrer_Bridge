package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.entities.UserProfile;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.domain.enums.TokenType;
import com.example.carrer_bridge.dto.request.AuthenticationRequest;
import com.example.carrer_bridge.dto.request.RegisterRequest;
import com.example.carrer_bridge.dto.response.AuthenticationResponse;
import com.example.carrer_bridge.handler.exception.OperationException;
import com.example.carrer_bridge.repository.RoleRepository;
import com.example.carrer_bridge.repository.UserRepository;
import com.example.carrer_bridge.security.jwt.JwtService;
import com.example.carrer_bridge.security.jwt.RefreshTokenService;
import com.example.carrer_bridge.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmedPassword())) {
            throw new OperationException("Confirmed password does not match");
        }
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new OperationException("User with this email already exists");
        }
        RoleType roleType = RoleType.valueOf(request.getRoleType());

        Role role = roleRepository.findByRoleType(roleType)
                .orElseThrow(() -> new OperationException("Role not found"));

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .build();

        user.setUserProfile(userProfile);

        user = userRepository.save(user);

        Set<String> authorities = user.getRole().getRolePermissions().stream()
                .map(rolePermission -> rolePermission.getPermission().getPermissionType().name())
                .collect(Collectors.toSet());
        user = userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());


        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType( TokenType.BEARER.name())
                .role(user.getRole().getRoleType().name())
                .authorities(authorities)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        String roleType = user.getRole().getRoleType().name();

        Set<String> authorities = user.getRole().getRolePermissions().stream()
                .map(rolePermission -> rolePermission.getPermission().getPermissionType().name())
                .collect(Collectors.toSet());

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType( TokenType.BEARER.name())
                .role(roleType)
                .authorities(authorities)
                .build();
    }
}