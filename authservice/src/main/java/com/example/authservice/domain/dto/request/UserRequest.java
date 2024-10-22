package com.example.authservice.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Password cannot be null")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$",
            message = "Password must be at least 6 characters long, " +
                    "and include at least one uppercase letter, one lowercase letter, and one number")
    private String password;
}
