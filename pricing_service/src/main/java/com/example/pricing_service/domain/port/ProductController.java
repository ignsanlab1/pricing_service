package com.example.pricing_service.domain.port;

import com.example.pricing_service.domain.dto.ProductDto;
import com.example.pricing_service.domain.dto.request.ProductRequest;
import org.springframework.http.ResponseEntity;

public interface ProductController {
    ResponseEntity<ProductRequest> getProductById(String id);
    ResponseEntity<ProductDto> createProduct(ProductDto productDto);
    ResponseEntity<Void> updateProduct(String id, ProductDto productDto);
    ResponseEntity<Void> deleteProductById(String id);
}
