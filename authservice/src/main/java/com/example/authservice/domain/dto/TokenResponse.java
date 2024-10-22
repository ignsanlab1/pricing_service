package com.example.authservice.domain.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String accessToken;
}
