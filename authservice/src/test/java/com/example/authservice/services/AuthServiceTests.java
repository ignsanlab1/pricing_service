package com.example.authservice.services;

import com.example.authservice.application.service.JwtService;
import com.example.authservice.infraestructure.commons.constants.RoleType;
import com.example.authservice.domain.dto.TokenResponse;
import com.example.authservice.domain.dto.request.UserRequest;
import com.example.authservice.infraestructure.entity.UserEntity;
import com.example.authservice.infraestructure.repository.UserJpaRepository;
import com.example.authservice.application.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTests {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthServiceImpl authService;

    private UserRequest userRequest;
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userRequest = new UserRequest("test@example.com", "Password123");
        userEntity = UserEntity.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("Password123"))
                .role(RoleType.ROLE_USER)
                .build();

        userJpaRepository.save(userEntity);
    }

    @Test
    public void testLoginUser_Successful() {
        TokenResponse response = authService.loginUser(userRequest);

        assertNotNull(response);
        assertNotNull(response.getAccessToken());
    }

    @Test
    public void testLoginUser_InvalidCredentials() {
        userRequest.setPassword("WrongPassword");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.loginUser(userRequest);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    public void testLoginUser_UserNotFound() {
        userRequest.setEmail("nonexistent@example.com");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.loginUser(userRequest);
        });

        assertEquals("Invalid credentials", exception.getMessage());
    }
}