package com.example.pricing_service.application.service.impl;

import com.example.pricing_service.application.service.BrandService;
import com.example.pricing_service.application.service.PriceService;
import com.example.pricing_service.application.service.ProductService;
import com.example.pricing_service.domain.dto.BrandDto;
import com.example.pricing_service.domain.dto.ProductDto;
import com.example.pricing_service.domain.dto.request.ProductRequest;
import com.example.pricing_service.domain.model.Brand;
import com.example.pricing_service.domain.model.Price;
import com.example.pricing_service.domain.model.Product;
import com.example.pricing_service.domain.port.BrandRepository;
import com.example.pricing_service.domain.port.ProductRepository;
import com.example.pricing_service.infraestructure.commons.exceptions.*;
import com.example.pricing_service.infraestructure.rest.mapper.BrandMapper;
import com.example.pricing_service.infraestructure.rest.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.pricing_service.infraestructure.commons.constants.ExceptionMessages.PRODUCT_NOT_FOUND;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final BrandService brandService;
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;
    private final PriceService priceService;

    public ProductServiceImpl(ProductRepository productRepository, BrandService brandService, BrandRepository brandRepository, BrandMapper brandMapper, ProductMapper productMapper, PriceService priceService) {
        this.productRepository = productRepository;
        this.brandService = brandService;
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
        this.productMapper = productMapper;
        this.priceService = priceService;
    }

    @Override
    public ProductRequest getProductById(String id) {
        return fetchProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND + " with ID: " + id));
    }

    /**
     * Retrieves the applicable price from the repository based on the given parameters.
     *
     * @param id The name .
     * @return An {@link Optional} containing a {@link ProductRequest} if a product is found, or empty if not.
     */
    private Optional<ProductRequest> fetchProductById(String id) {
        return productRepository.findProductById(id)
                .map(productMapper::toProductRequest);
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        List<Long> brandIds = productDto.getBrandIds();
        List<Brand> existingBrands = brandRepository.findAllById(brandIds);

        if (existingBrands.size() != brandIds.size()) {
            throw new BrandNotFoundException("Some of the provided brands do not exist");
        }

        Product newProduct = productMapper.toProductFromDto(productDto);

        return Optional.of(newProduct)
                .filter(product -> !productRepository.existsByHashCode(product.hashCode()))
                .map(product -> {
                    Product savedProduct = productRepository.saveProduct(product);
                    updateBrandsWithNewProduct(brandIds, savedProduct.getId());
                    return savedProduct;
                })
                .map(productMapper::toProductDto)
                .orElseThrow(() -> new ProductAlreadyExistsException("A product with the same attributes already exists"));
    }

    /**
     * Update the brands associated with the new product using the join table.
     *
     * @param brandIds List of IDs of the brands to be associated.
     * @param productId ID of the newly created product.
     */
    private void updateBrandsWithNewProduct(List<Long> brandIds, Long productId) {
        Product product = productRepository.findProductById(String.valueOf(productId)).orElseThrow(() ->
                new ProductNotFoundException("Product not found"));

        List<Brand> currentBrands = brandRepository.findBrandsByProductId(productId);

        currentBrands.forEach(brand -> {
            if (!brandIds.contains(brand.getId())) {
                brand.getProductList().removeIf(p -> p.getId().equals(product.getId()));
                BrandDto brandDto = brandMapper.toBrandDto(brand);
                List<Long> updatedProductIds = brand.getProductList().stream()
                        .map(Product::getId)
                        .toList();
                brandDto.setProductIds(updatedProductIds);
                brandService.updateBrand(String.valueOf(brand.getId()), brandDto);
            }
        });

        brandIds.forEach(brandId -> {
            Brand brand = brandRepository.findBrandById(String.valueOf(brandId)).orElseThrow(() ->
                    new BrandNotFoundException("Brand not found with ID: " + brandId));

            if (!brand.getProductList().contains(product)) {
                brand.getProductList().add(product);
                brandRepository.saveBrand(brand);
            }
        });
    }

    @Override
    @Transactional
    public ProductDto updateProduct(String id, ProductDto productDto) {
        List<Long> brandIds = productDto.getBrandIds();
        List<Long> priceIds = productDto.getPriceIds();
        return productRepository.findProductById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDto.getName());
                    existingProduct.setUserId(productDto.getUserId());
                    existingProduct.setCategory(productDto.getCategory());
                    List<Price> prices = priceService.findPricesByIds(priceIds);
                    existingProduct.setPriceList(prices);
                    updateBrandsWithNewProduct(brandIds, existingProduct.getId());
                    return productRepository.saveProduct(existingProduct);
                })
                .map(productMapper::toProductDto)
                .orElseThrow(() -> new ProductNotFoundException("Error updating Product with ID " + id + PRODUCT_NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteProductById(String id) {
        fetchProductById(id)
                .map(existingProduct -> {
                    productRepository.deleteById(Long.parseLong(id));
                    return existingProduct;
                })
                .orElseThrow(() -> new ProductNotFoundException("Error deleting Product with ID " + id + PRODUCT_NOT_FOUND));
    }
}
