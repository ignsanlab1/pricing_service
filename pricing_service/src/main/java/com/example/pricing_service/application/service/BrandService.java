package com.example.pricing_service.application.service;

import com.example.pricing_service.domain.dto.BrandDto;
import com.example.pricing_service.domain.dto.request.BrandRequest;

public interface BrandService {
    BrandRequest getBrandById(String id);

    BrandRequest getBrandByName(String name);

    BrandDto createBrand(BrandDto brandDto);

    BrandDto updateBrand(String id, BrandDto brandDto);

    void deleteBrandById(String id);

    void deleteBrandByName(String name);
}
