package com.example.pricing_service.infraestructure.adapters;

import com.example.pricing_service.domain.model.Brand;
import com.example.pricing_service.domain.port.BrandRepository;
import com.example.pricing_service.infraestructure.commons.exceptions.BrandBadRequestException;
import com.example.pricing_service.infraestructure.repository.BrandJpaRepository;
import com.example.pricing_service.infraestructure.rest.mapper.BrandMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BrandRepositoryImpl implements BrandRepository {
    private final BrandJpaRepository brandJpaRepository;
    private final BrandMapper brandMapper;

    public BrandRepositoryImpl(BrandJpaRepository brandJpaRepository, BrandMapper brandMapper) {
        this.brandJpaRepository = brandJpaRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    public Optional<Brand> findBrandByName(String name) {
        return brandJpaRepository.findByName(name)
                .map(brandMapper::toBrand);
    }

    @Override
    public Optional<Brand> findBrandById(String id) {
        return brandJpaRepository.findById(Long.valueOf(id))
                .map(brandMapper::toBrand);
    }

    @Override
    public Brand saveBrand(Brand brand) {
        return Optional.ofNullable(brand)
                .map(brandMapper::toBrandEntity)
                .map(brandJpaRepository::save)
                .map(brandMapper::toBrand)
                .orElseThrow(() -> new BrandBadRequestException("Error saving the brand"));
    }

    @Override
    public void deleteById(long id) {
        brandJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByName(String name) {
        brandJpaRepository.deleteByName(name);
    }

    @Override
    public List<Brand> findAllById(List<Long> brandIds) {
        return brandJpaRepository.findAllById(brandIds).stream()
                .map(brandMapper::toBrand)
                .toList();
    }

    @Override
    public List<Brand> findBrandsByProductId(Long productId) {
        return brandJpaRepository.findBrandsByProductId(productId).stream()
                .map(brandMapper::toBrand)
                .toList();
    }
}
