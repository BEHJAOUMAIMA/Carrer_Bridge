package com.example.carrer_bridge.test;

import com.example.carrer_bridge.domain.entities.Role;
import com.example.carrer_bridge.domain.entities.User;
import com.example.carrer_bridge.domain.enums.RoleType;
import com.example.carrer_bridge.dto.request.AuthenticationRequest;
import com.example.carrer_bridge.dto.request.RegisterRequest;
import com.example.carrer_bridge.dto.response.AuthenticationResponse;
import com.example.carrer_bridge.repository.RoleRepository;
import com.example.carrer_bridge.repository.UserRepository;
import com.example.carrer_bridge.security.jwt.JwtService;
import com.example.carrer_bridge.security.jwt.RefreshTokenService;
import com.example.carrer_bridge.service.impl.AuthenticationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("oumaima");
        request.setLastName("behja");
        request.setEmail("oumaima@gmail.com");
        request.setPassword("Azerty");
        request.setConfirmedPassword("Azerty");
        request.setRoleType("PROFISSIONAL");

        Role role = new Role();
        role.setRoleType(RoleType.ADMINISTRATOR);

        when(roleRepository.findByRoleType(RoleType.ADMINISTRATOR)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        // When
        AuthenticationResponse response = authenticationService.register(request);

        // Then
        assertEquals("John", response.getFirstname());
        assertEquals("Doe", response.getLastname());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("BEARER", response.getTokenType());
        assertEquals("ADMINISTRATOR", response.getRole());
    }

    @Test
    public void testAuthenticate() {
        // Given
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("john@example.com");
        request.setPassword("password");

        Role role = new Role();
        role.setRoleType(RoleType.ADMINISTRATOR);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setRole(role);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        // When
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Then
        assertEquals("John", response.getFirstname());
        assertEquals("Doe", response.getLastname());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("BEARER", response.getTokenType());
        assertEquals("ADMINISTRATOR", response.getRole());
    }
}
