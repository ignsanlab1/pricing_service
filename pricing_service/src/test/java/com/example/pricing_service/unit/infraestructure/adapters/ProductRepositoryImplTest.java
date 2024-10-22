package com.example.pricing_service.unit.infraestructure.adapters;

import com.example.pricing_service.domain.model.Product;
import com.example.pricing_service.infraestructure.adapters.ProductRepositoryImpl;
import com.example.pricing_service.infraestructure.commons.exceptions.ProductBadRequestException;
import com.example.pricing_service.infraestructure.entity.ProductEntity;
import com.example.pricing_service.infraestructure.repository.ProductJpaRepository;
import com.example.pricing_service.infraestructure.rest.mapper.ProductMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductRepositoryImplTest {

    @Mock
    private ProductJpaRepository productJpaRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductRepositoryImpl productRepositoryImpl;

    private Product product;
    private ProductEntity productEntity;

    @BeforeEach
    public void setUp() {

        product = Product.builder()
                .id(100L)
                .name("Test Product")
                .userId(1L)
                .category(null)
                .priceList(Collections.emptyList())
                .build();

        productEntity = ProductEntity.builder()
                .id(100L)
                .name("Test Product")
                .userId(1L)
                .category(null)
                .priceList(Collections.emptyList())
                .build();
    }

    @Test
    @Order(1)
    void test01FindProductById_Success() {
        when(productJpaRepository.findById(100L)).thenReturn(Optional.of(productEntity));
        when(productMapper.toProduct(productEntity)).thenReturn(product);

        Optional<Product> result = productRepositoryImpl.findProductById("100");

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(productJpaRepository).findById(100L);
        verify(productMapper).toProduct(productEntity);
    }

    @Test
    @Order(2)
    void test02FindProductById_NotFound() {
        when(productJpaRepository.findById(100L)).thenReturn(Optional.empty());

        Optional<Product> result = productRepositoryImpl.findProductById("100");

        assertFalse(result.isPresent());
        verify(productJpaRepository).findById(100L);
        verify(productMapper, never()).toProduct(any());
    }

    @Test
    @Order(3)
    void test03SaveProduct_Success() {
        when(productMapper.toProductEntity(product)).thenReturn(productEntity);
        when(productJpaRepository.save(productEntity)).thenReturn(productEntity);
        when(productMapper.toProduct(productEntity)).thenReturn(product);

        Product result = productRepositoryImpl.saveProduct(product);

        assertEquals(product, result);
        verify(productMapper).toProductEntity(product);
        verify(productJpaRepository).save(productEntity);
        verify(productMapper).toProduct(productEntity);
    }

    @Test
    @Order(4)
    void test04SaveProduct_NullProduct() {
        Exception exception = assertThrows(ProductBadRequestException.class, () -> {
            productRepositoryImpl.saveProduct(null);
        });

        assertEquals("Error saving the product", exception.getMessage());
    }

    @Test
    @Order(5)
    void test05DeleteById() {
        productRepositoryImpl.deleteById(100L);
        verify(productJpaRepository).deleteById(100L);
    }

    @Test
    @Order(6)
    void test06ExistsByHashCode() {
        when(productJpaRepository.existsByHashCode(12345)).thenReturn(true);

        boolean exists = productRepositoryImpl.existsByHashCode(12345);

        assertTrue(exists);
        verify(productJpaRepository).existsByHashCode(12345);
    }

    @Test
    @Order(7)
    void test07FindAllById() {
        when(productJpaRepository.findAllById(anyList())).thenReturn(List.of(productEntity));
        when(productMapper.toProduct(productEntity)).thenReturn(product);

        List<Product> result = productRepositoryImpl.findAllById(List.of(100L));

        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
        verify(productJpaRepository).findAllById(anyList());
        verify(productMapper).toProduct(productEntity);
    }
}
