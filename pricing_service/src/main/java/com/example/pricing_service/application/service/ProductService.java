package com.example.pricing_service.application.service;

import com.example.pricing_service.domain.dto.ProductDto;
import com.example.pricing_service.domain.dto.request.ProductRequest;

public interface ProductService {
    ProductRequest getProductById(String id);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(String id, ProductDto productDto);

    void deleteProductById(String id);
}
