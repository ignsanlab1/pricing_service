package com.example.pricing_service.domain.port;

import com.example.pricing_service.domain.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    Optional<Brand> findBrandByName(String name);

    Optional<Brand> findBrandById(String id);

    Brand saveBrand(Brand brand);

    void deleteById(long id);

    void deleteByName(String name);

    List<Brand> findAllById(List<Long> brandIds);

    List<Brand> findBrandsByProductId(Long productId);
}
