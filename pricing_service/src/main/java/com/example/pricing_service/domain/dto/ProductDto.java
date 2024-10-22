package com.example.pricing_service.domain.dto;

import com.example.pricing_service.infraestructure.commons.constants.CategoryType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductDto {
    private Long id;
    @NotNull(message = "Name cannot be null")
    private String name;
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    @NotNull(message = "Category cannot be null")
    private CategoryType category;
    @NotEmpty(message = "Brand list cannot be empty")
    private List<Long> brandIds;
    private List<Long> priceIds;
}
