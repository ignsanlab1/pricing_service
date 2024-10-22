package com.example.pricing_service.domain.port;

import com.example.pricing_service.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findProductById(String id);

    Product saveProduct(Product product);

    void deleteById(long id);

    boolean existsByHashCode(int hashCode);

    List<Product> findAllById(List<Long> productIds);
}
