package com.example.pricing_service.domain.dto.request;

import com.example.pricing_service.domain.model.Price;
import com.example.pricing_service.infraestructure.commons.constants.CategoryType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductRequest {
    private String name;
    private CategoryType category;
    private List<Price> priceList;
}
