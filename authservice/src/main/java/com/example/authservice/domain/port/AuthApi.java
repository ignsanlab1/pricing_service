package com.example.authservice.domain.port;

import com.example.authservice.infraestructure.commons.constants.ApiPathConstants;
import com.example.authservice.domain.dto.TokenResponse;
import com.example.authservice.domain.dto.request.UserRequest;
import com.example.authservice.domain.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE)
public interface AuthApi {
    ResponseEntity<TokenResponse> createUser(@RequestBody @Valid UserRequest userRequest);
    ResponseEntity<TokenResponse> loginUser(@RequestBody @Valid UserRequest userRequest);
    ResponseEntity<UserResponse> getUser(@PathVariable(name = "userId") String userId);
}
