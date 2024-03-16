package com.example.carrer_bridge.security.jwt;

import com.example.carrer_bridge.domain.entities.RefreshToken;
import com.example.carrer_bridge.dto.request.RefreshTokenRequest;
import com.example.carrer_bridge.dto.response.RefreshTokenResponse;
import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId);
    RefreshToken verifyExpiration(RefreshToken token);
    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);

}
