package com.example.authservice.application.service;

import com.example.authservice.domain.dto.TokenResponse;
import io.jsonwebtoken.Claims;

public interface JwtService {
    TokenResponse generateToken(Long userId);

    Claims getClaims(String token);

    boolean isExpired(String token);

    Integer extractUserId(String token);

}
