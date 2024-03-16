package com.example.carrer_bridge.service.impl;

import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.enums.TokenType;
import com.example.carrer_bridge.dto.request.AuthenticationRequest;
import com.example.carrer_bridge.dto.request.RegisterRequest;
import com.example.carrer_bridge.dto.response.AuthenticationResponse;
import com.example.carrer_bridge.handler.exception.OperationException;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
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

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());


        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType( TokenType.BEARER.name())
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType( TokenType.BEARER.name())
                .build();
    }
}
