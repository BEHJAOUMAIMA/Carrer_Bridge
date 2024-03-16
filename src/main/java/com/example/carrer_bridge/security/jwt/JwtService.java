package com.example.carrer_bridge.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface JwtService {
    String extractUsername(String token);
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    Boolean isTokenValid(String token, UserDetails userDetails);
    public Long getUserIdFromToken(String token);
}
