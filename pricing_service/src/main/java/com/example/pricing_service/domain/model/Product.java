package com.example.pricing_service.domain.model;

import com.example.pricing_service.infraestructure.commons.constants.CategoryType;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    private Long id;
    private String name;
    private Long userId;
    private CategoryType category;
    private List<Price> priceList;
}
