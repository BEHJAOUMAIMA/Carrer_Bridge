package com.example.carrer_bridge.service;

import com.example.carrer_bridge.dto.request.AuthenticationRequest;
import com.example.carrer_bridge.dto.request.RegisterRequest;
import com.example.carrer_bridge.dto.response.AuthenticationResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);

}
