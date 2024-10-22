package com.example.pricing_service.infraestructure.adapters;

import com.example.pricing_service.domain.model.Product;
import com.example.pricing_service.domain.port.ProductRepository;
import com.example.pricing_service.infraestructure.commons.exceptions.ProductBadRequestException;
import com.example.pricing_service.infraestructure.repository.ProductJpaRepository;
import com.example.pricing_service.infraestructure.rest.mapper.BrandMapper;
import com.example.pricing_service.infraestructure.rest.mapper.ProductMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final ProductMapper productMapper;
    private final BrandMapper brandMapper;

    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository, ProductMapper productMapper, BrandMapper brandMapper) {
        this.productJpaRepository = productJpaRepository;
        this.productMapper = productMapper;
        this.brandMapper = brandMapper;
    }

    @Override
    public Optional<Product> findProductById(String id) {
        return productJpaRepository.findById(Long.valueOf(id))
                .map(productMapper::toProduct);
    }

    @Override
    public Product saveProduct(Product product) {
        return Optional.ofNullable(product)
                .map(productMapper::toProductEntity)
                .map(productJpaRepository::save)
                .map(productMapper::toProduct)
                .orElseThrow(() -> new ProductBadRequestException("Error saving the product"));
    }

    @Override
    public void deleteById(long id) {
        productJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByHashCode(int hashCode) {
        return productJpaRepository.existsByHashCode(hashCode);
    }

    @Override
    public List<Product> findAllById(List<Long> productIds) {
        return productJpaRepository.findAllById(productIds).stream()
                .map(productMapper::toProduct)
                .toList();
    }
}
