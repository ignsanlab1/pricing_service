package com.example.authservice.infraestructure.rest.controller;

import com.example.authservice.domain.dto.TokenResponse;
import com.example.authservice.domain.dto.request.UserRequest;
import com.example.authservice.domain.dto.UserResponse;
import com.example.authservice.domain.port.AuthApi;
import com.example.authservice.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value= "/register")
    @Operation(summary = "Register a new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error occurred while register user")
    })
    @Override
    public ResponseEntity<TokenResponse> createUser(UserRequest userRequest) {
        return ResponseEntity.ok(authService.createUser(userRequest));
    }

    @PostMapping(value= "/login")
    @Operation(summary = "Login a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error - Error occurred while logging user")
    })
    @Override
    public ResponseEntity<TokenResponse> loginUser(UserRequest userRequest) {
        return ResponseEntity.ok(authService.loginUser(userRequest));
    }

    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get User by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Access denied")
    })
    @Override
    public ResponseEntity<UserResponse> getUser(String userId) {
        UserResponse user = authService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

}
