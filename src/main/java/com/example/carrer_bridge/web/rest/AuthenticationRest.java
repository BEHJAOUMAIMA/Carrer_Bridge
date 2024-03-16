package com.example.carrer_bridge.web.rest;

import com.example.carrer_bridge.dto.request.AuthenticationRequest;
import com.example.carrer_bridge.dto.request.RefreshTokenRequest;
import com.example.carrer_bridge.dto.request.RegisterRequest;
import com.example.carrer_bridge.dto.response.AuthenticationResponse;
import com.example.carrer_bridge.dto.response.RefreshTokenResponse;
import com.example.carrer_bridge.security.jwt.RefreshTokenService;
import com.example.carrer_bridge.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationRest {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenService.generateNewToken(request));
    }

}
