package com.example.authservice.application.service;

import com.example.authservice.domain.dto.TokenResponse;
import com.example.authservice.domain.dto.request.UserRequest;
import com.example.authservice.domain.dto.UserResponse;

public interface AuthService {
    TokenResponse createUser(UserRequest userRequest);
    TokenResponse loginUser(UserRequest userRequest);
    UserResponse getUserById(String userId);
}
