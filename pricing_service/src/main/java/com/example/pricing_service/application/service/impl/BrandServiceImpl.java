package com.example.pricing_service.application.service.impl;

import com.example.pricing_service.application.service.BrandService;
import com.example.pricing_service.domain.dto.BrandDto;
import com.example.pricing_service.domain.dto.request.BrandRequest;
import com.example.pricing_service.domain.model.Brand;
import com.example.pricing_service.domain.model.Product;
import com.example.pricing_service.domain.port.BrandRepository;
import com.example.pricing_service.domain.port.ProductRepository;
import com.example.pricing_service.infraestructure.commons.exceptions.BrandNotFoundException;
import com.example.pricing_service.infraestructure.rest.mapper.BrandMapper;
import com.example.pricing_service.infraestructure.rest.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.pricing_service.infraestructure.commons.constants.ExceptionMessages.BRAND_NOT_FOUND;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository  brandRepository;
    private final ProductRepository productRepository;
    private final BrandMapper brandMapper;

    public BrandServiceImpl(BrandRepository brandRepository, ProductRepository productRepository, ProductMapper productMapper, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
        this.brandMapper = brandMapper;
    }


    @Override
    public BrandRequest getBrandById(String id) {
        return fetchBrandById(id)
                .orElseThrow(() -> new BrandNotFoundException(BRAND_NOT_FOUND + " with ID: " + id));
    }

    @Override
    public BrandRequest getBrandByName(String name) {
        return fetchBrandByName(name)
                .orElseThrow(() -> new BrandNotFoundException(BRAND_NOT_FOUND + " with name: " + name));
    }

    /**
     * Retrieves the applicable price from the repository based on the given parameters.
     *
     * @param name The name .
     * @return An {@link Optional} containing a {@link BrandRequest} if a brand is found, or empty if not.
     */
    private Optional<BrandRequest> fetchBrandByName(String name) {
        return brandRepository.findBrandByName(name)
                .map(brandMapper::toBrandRequest);
    }

    /**
     * Retrieves the applicable price from the repository based on the given parameters.
     *
     * @param id The name .
     * @return An {@link Optional} containing a {@link BrandRequest} if a brand is found, or empty if not.
     */
    private Optional<BrandRequest> fetchBrandById(String id) {
        return brandRepository.findBrandById(id)
                .map(brandMapper::toBrandRequest);
    }


    @Override
    @Transactional
    public BrandDto createBrand(BrandDto brandDto) {
        Brand brand = brandMapper.toBrandFromDto(brandDto);

        List<Product> products = Optional.ofNullable(brandDto.getProductIds())
                .filter(ids -> !ids.isEmpty())
                .map(productRepository::findAllById)
                .orElse(Collections.emptyList());

        brand.setProductList(new ArrayList<>(products));

        Brand savedBrand = brandRepository.saveBrand(brand);

        return brandMapper.toBrandDto(savedBrand);
    }

    @Override
    @Transactional
    public BrandDto updateBrand(String id, BrandDto brandDto) {
        if (!id.equals(brandDto.getId().toString())) {
            throw new IllegalArgumentException("It is not possible to change the brand ID. The ID provided is: " + brandDto.getId());
        }
        return brandRepository.findBrandById(id)
                .map(existingBrand -> {
                    existingBrand.setName(brandDto.getName());
                    List<Product> products = productRepository.findAllById(brandDto.getProductIds());
                    existingBrand.setProductList(products);
                    existingBrand.setUserId(brandDto.getUserId());
                    return brandRepository.saveBrand(existingBrand);
                })
                .map(brandMapper::toBrandDto)
                .orElseThrow(() -> new BrandNotFoundException("Error updating Brand with ID " + id + BRAND_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteBrandById(String id) {
        fetchBrandById(id)
                .map(existingBrand -> {
                    brandRepository.deleteById(Long.parseLong(id));
                    return existingBrand;
                })
                .orElseThrow(() -> new BrandNotFoundException("Error deleting Brand with ID " + id + BRAND_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteBrandByName(String name) {
        fetchBrandByName(name)
                .map(existingBrand -> {
                    brandRepository.deleteByName(name);
                    return existingBrand;
                })
                .orElseThrow(() -> new BrandNotFoundException("Error deleting Brand with name " + name + BRAND_NOT_FOUND));
    }

}
