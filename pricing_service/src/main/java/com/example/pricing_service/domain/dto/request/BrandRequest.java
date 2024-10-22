package com.example.pricing_service.domain.dto.request;

import com.example.pricing_service.domain.model.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BrandRequest {
    private String name;
    private List<Product> productList;
}
