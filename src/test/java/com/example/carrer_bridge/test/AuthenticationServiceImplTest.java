package com.example.carrer_bridge.test;

import com.example.carrer_bridge.domain.entities.RefreshToken;
import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.entities.UserProfile;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.dto.request.AuthenticationRequest;
import com.example.carrer_bridge.dto.request.RegisterRequest;
import com.example.carrer_bridge.dto.response.AuthenticationResponse;
import com.example.carrer_bridge.repository.RoleRepository;
import com.example.carrer_bridge.repository.UserRepository;
import com.example.carrer_bridge.security.jwt.JwtService;
import com.example.carrer_bridge.security.jwt.RefreshTokenService;
import com.example.carrer_bridge.service.AuthenticationService;
import com.example.carrer_bridge.service.impl.AuthenticationServiceImpl;

import org.junit.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthenticationServiceImplTest {


    @Test
    public void test_register_new_user_with_valid_input_data() {

        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtService jwtService = mock(JwtService.class);
        UserRepository userRepository = mock(UserRepository.class);
        RoleRepository roleRepository = mock(RoleRepository.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        RefreshTokenService refreshTokenService = mock(RefreshTokenService.class);

        AuthenticationService authenticationService = new AuthenticationServiceImpl(passwordEncoder, jwtService, userRepository, roleRepository, authenticationManager, refreshTokenService);

        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .roleType("PROFESSIONAL")
                .password("password")
                .confirmedPassword("password")
                .build();

        Role role = Role.builder()
                .id(1L)
                .roleType(RoleType.PROFESSIONAL)
                .build();

        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role(role)
                .build();

        UserProfile userProfile = UserProfile.builder()
                .id(1L)
                .profileImage(null)
                .bio(null)
                .user(user)
                .build();

        AuthenticationResponse expectedResponse = AuthenticationResponse.builder()
                .id(1L)
                .email("john.doe@example.com")
                .accessToken("jwtToken")
                .refreshToken("refreshToken")
                .tokenType("BEARER")
                .role("PROFESSIONAL")
                .authorities(Collections.emptySet())
                .build();

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findByRoleType(RoleType.PROFESSIONAL)).thenReturn(Optional.of(role));
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(refreshTokenService.createRefreshToken(1L)).thenReturn(new RefreshToken());

        AuthenticationResponse response = authenticationService.register(request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void test_authenticate_user_with_valid_email_and_password() {

        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtService jwtService = mock(JwtService.class);
        UserRepository userRepository = mock(UserRepository.class);
        RoleRepository roleRepository = mock(RoleRepository.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        RefreshTokenService refreshTokenService = mock(RefreshTokenService.class);


        AuthenticationService authenticationService = new AuthenticationServiceImpl(passwordEncoder, jwtService, userRepository, roleRepository, authenticationManager, refreshTokenService);


        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("john.doe@example.com")
                .password("password")
                .build();


        Role role = Role.builder()
                .id(1L)
                .roleType(RoleType.PROFESSIONAL)
                .build();


        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role(role)
                .build();


        AuthenticationResponse expectedResponse = AuthenticationResponse.builder()
                .id(1L)
                .email("john.doe@example.com")
                .accessToken("jwtToken")
                .refreshToken("refreshToken")
                .tokenType("BEARER")
                .role("PROFESSIONAL")
                .authorities(Collections.emptySet())
                .build();

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(refreshTokenService.createRefreshToken(1L)).thenReturn(new RefreshToken());

        AuthenticationResponse response = authenticationService.authenticate(request);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void test_generate_jwt_and_refresh_token() {

        JwtService jwtService = mock(JwtService.class);
        UserRepository userRepository = mock(UserRepository.class);
        RefreshTokenService refreshTokenService = mock(RefreshTokenService.class);

        AuthenticationService authenticationService = new AuthenticationServiceImpl(null, jwtService, userRepository, null, null, refreshTokenService);

        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();


        RefreshToken refreshToken = RefreshToken.builder()
                .id(1L)
                .user(user)
                .token("refreshToken")
                .expiryDate(Instant.now())
                .revoked(false)
                .build();

        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(refreshTokenService.createRefreshToken(1L)).thenReturn(refreshToken);

        // Call the register method and assert the response
        AuthenticationResponse response = authenticationService.register(RegisterRequest.builder().build());
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    public void test_save_new_user_to_database() {

        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        JwtService jwtService = mock(JwtService.class);
        UserRepository userRepository = mock(UserRepository.class);
        RoleRepository roleRepository = mock(RoleRepository.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        RefreshTokenService refreshTokenService = mock(RefreshTokenService.class);


        AuthenticationService authenticationService = new AuthenticationServiceImpl(passwordEncoder, jwtService, userRepository, roleRepository, authenticationManager, refreshTokenService);


        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .roleType("USER")
                .password("password")
                .confirmedPassword("password")
                .build();


        Role role = Role.builder()
                .id(1L)
                .roleType(RoleType.PROFESSIONAL)
                .build();


        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role(role)
                .build();


        UserProfile userProfile = UserProfile.builder()
                .id(1L)
                .profileImage(null)
                .bio(null)
                .user(user)
                .build();


        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findByRoleType(RoleType.PROFESSIONAL)).thenReturn(Optional.of(role));
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);


        AuthenticationResponse response = authenticationService.register(request);
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals(1L, response.getId().longValue());
    }



}