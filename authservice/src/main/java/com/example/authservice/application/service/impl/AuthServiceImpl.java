package com.example.authservice.application.service.impl;

import com.example.authservice.domain.model.User;
import com.example.authservice.domain.port.UserRepository;
import com.example.authservice.infraestructure.commons.constants.RoleType;
import com.example.authservice.domain.dto.TokenResponse;
import com.example.authservice.domain.dto.request.UserRequest;
import com.example.authservice.domain.dto.UserResponse;
import com.example.authservice.application.service.AuthService;
import com.example.authservice.application.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TokenResponse createUser(UserRequest userRequest) {
        return Optional.of(userRequest)
                .map(this::mapToDomain)
                .map(userRepository::save)
                .map(userCreated -> jwtService.generateToken(userCreated.getId()))
                .orElseThrow(() -> new RuntimeException("Error creating user"));
    }

    @Override
    public TokenResponse loginUser(UserRequest userRequest) {
        return userRepository.findByEmail(userRequest.getEmail())
                .filter(user -> passwordEncoder.matches(userRequest.getPassword(), user.getPassword()))
                .map(User::getId)
                .map(jwtService::generateToken)
                .map(token -> TokenResponse.builder().accessToken(token.getAccessToken()).build())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    private User mapToDomain(UserRequest userRequest){
        return User.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(RoleType.ROLE_USER)
                .build();
    }

    @Override
    public UserResponse getUserById(String userId) {
        User user= userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}
