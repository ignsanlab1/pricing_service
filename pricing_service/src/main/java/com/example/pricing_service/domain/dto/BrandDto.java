package com.example.pricing_service.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BrandDto {

    private Long id;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "user ID cannot be null")
    private Long userId;

    private List<Long> productIds;
}
