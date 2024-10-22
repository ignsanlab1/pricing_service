package com.example.pricing_service.unit.application.service;

import com.example.pricing_service.application.service.PriceService;
import com.example.pricing_service.application.service.impl.ProductServiceImpl;
import com.example.pricing_service.domain.dto.BrandDto;
import com.example.pricing_service.domain.dto.ProductDto;
import com.example.pricing_service.domain.dto.request.ProductRequest;
import com.example.pricing_service.domain.model.Brand;
import com.example.pricing_service.domain.model.Price;
import com.example.pricing_service.domain.model.Product;
import com.example.pricing_service.domain.port.BrandRepository;
import com.example.pricing_service.domain.port.ProductRepository;
import com.example.pricing_service.infraestructure.commons.exceptions.BrandNotFoundException;
import com.example.pricing_service.infraestructure.commons.exceptions.ProductNotFoundException;
import com.example.pricing_service.infraestructure.rest.mapper.ProductMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private PriceService priceService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private Product mockProduct;

    @Mock
    private ProductDto mockProductDto;

    private ProductRequest mockProductRequest;
    private BrandDto mockBrandDto;
    private Brand mockBrand;

    @BeforeEach
    void setUp() {
        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Product1");

        mockBrand = new Brand();
        mockBrand.setId(1L);
        mockBrand.setProductList(new ArrayList<>(Arrays.asList(mockProduct)));

        mockProductDto = new ProductDto();
        mockProductDto.setPriceIds(Arrays.asList(1L, 2L));
        mockProductDto.setBrandIds(Arrays.asList(1L));
        mockProductDto.setId(1L);

        mockBrandDto = BrandDto.builder()
                .userId(1L)
                .name("BrandName")
                .productIds(Arrays.asList(mockProductDto.getId()))
                .id(1L)
                .build();

        mockProductRequest = ProductRequest.builder()
                .name("SomeProduct")
                .build();
    }

    @Test
    @Order(1)
    void test01GetProductById_Success() {
        String productId = "1";

        when(productRepository.findProductById(productId)).thenReturn(Optional.of(mockProduct));
        when(productMapper.toProductRequest(mockProduct)).thenReturn(new ProductRequest());

        ProductRequest result = productService.getProductById(productId);

        assertNotNull(result);
        verify(productRepository).findProductById(productId);
    }

    @Test
    @Order(2)
    void test02GetProductById_ProductNotFound() {
        String productId = "1";

        when(productRepository.findProductById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(productId);
        });

        verify(productRepository).findProductById(productId);
    }

    @Test
    @Order(3)
    void test03CreateProduct_Success() {
        when(brandRepository.findAllById(mockProductDto.getBrandIds())).thenReturn(List.of(mockBrand));
        when(productMapper.toProductFromDto(mockProductDto)).thenReturn(mockProduct);
        when(productRepository.existsByHashCode(mockProduct.hashCode())).thenReturn(false);
        when(productRepository.saveProduct(mockProduct)).thenReturn(mockProduct);
        when(productMapper.toProductDto(mockProduct)).thenReturn(mockProductDto);
        when(brandRepository.findBrandsByProductId(mockProduct.getId())).thenReturn(List.of(mockBrand));
        when(brandRepository.findBrandById(anyString())).thenReturn(Optional.of(mockBrand));
        when(productRepository.findProductById(String.valueOf(mockProduct.getId()))).thenReturn(Optional.of(mockProduct));

        ProductDto result = productService.createProduct(mockProductDto);
        assertNotNull(result);

        verify(productRepository).saveProduct(mockProduct);
        verify(brandRepository).findAllById(mockProductDto.getBrandIds());
        verify(brandRepository).findBrandsByProductId(mockProduct.getId());
        verify(brandRepository, times(mockProductDto.getBrandIds().size())).findBrandById(anyString());
    }

    @Test
    @Order(4)
    void test04CreateProduct_BrandNotFound() {
        when(brandRepository.findAllById(mockProductDto.getBrandIds())).thenReturn(List.of());

        assertThrows(BrandNotFoundException.class, () -> {
            productService.createProduct(mockProductDto);
        });

        verify(brandRepository).findAllById(mockProductDto.getBrandIds());
    }

    @Test
    @Order(5)
    void test05UpdateProduct_Success() {
        String productId = "1";

        when(productRepository.saveProduct(mockProduct)).thenReturn(mockProduct);
        when(productMapper.toProductDto(mockProduct)).thenReturn(mockProductDto);
        when(brandRepository.findBrandsByProductId(mockProduct.getId())).thenReturn(List.of(mockBrand));
        when(brandRepository.findBrandById(anyString())).thenReturn(Optional.of(mockBrand));
        when(productRepository.findProductById(String.valueOf(mockProduct.getId()))).thenReturn(Optional.of(mockProduct));
        when(priceService.findPricesByIds(mockProductDto.getPriceIds())).thenReturn(List.of(new Price(), new Price()));

        ProductDto result = productService.updateProduct(productId, mockProductDto);

        assertNotNull(result);
        verify(productRepository, times(2)).findProductById(productId);
        verify(priceService).findPricesByIds(mockProductDto.getPriceIds());
        verify(productRepository).saveProduct(mockProduct);
    }

    @Test
    @Order(6)
    void test06UpdateProduct_ProductNotFound() {
        String productId = "1";

        when(productRepository.findProductById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(productId, mockProductDto);
        });

        verify(productRepository).findProductById(productId);
    }

    @Test
    @Order(7)
    void test07DeleteProductById_Success() {

        when(productRepository.findProductById(String.valueOf(mockProduct.getId()))).thenReturn(Optional.of(mockProduct));
        when(productMapper.toProductRequest(mockProduct)).thenReturn(mockProductRequest);

        productService.deleteProductById(String.valueOf(mockProduct.getId()));

        verify(productRepository).deleteById(Long.parseLong(String.valueOf(mockProduct.getId())));
    }

    @Test
    @Order(8)
    void test08DeleteProductById_ProductNotFound() {
        String productId = "1";

        when(productRepository.findProductById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProductById(productId);
        });

        verify(productRepository, never()).deleteById(anyLong());
    }
}