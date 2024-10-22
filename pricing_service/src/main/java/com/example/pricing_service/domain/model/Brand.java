package com.example.pricing_service.domain.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Brand {
    private Long id;
    private String name;
    private Long userId;
    private List<Product> productList;
}
