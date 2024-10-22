package com.example.authservice.domain.dto;

import com.example.authservice.infraestructure.commons.constants.RoleType;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String email;
    private String name;
    private RoleType role;
}
