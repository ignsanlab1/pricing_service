package com.example.pricing_service.unit.infraestructure.rest.mapper;

import com.example.pricing_service.domain.dto.ProductDto;
import com.example.pricing_service.domain.dto.request.ProductRequest;
import com.example.pricing_service.domain.model.Product;
import com.example.pricing_service.infraestructure.entity.ProductEntity;
import com.example.pricing_service.infraestructure.rest.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = Mappers.getMapper(ProductMapper.class);
    }

    @Test
    void testToProduct() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("SomeProduct");

        Product product = productMapper.toProduct(productEntity);

        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(productEntity.getId());
        assertThat(product.getName()).isEqualTo(productEntity.getName());
    }

    @Test
    void testToProducts() {
        ProductEntity productEntity1 = new ProductEntity();
        productEntity1.setId(1L);
        productEntity1.setName("Product1");

        ProductEntity productEntity2 = new ProductEntity();
        productEntity2.setId(2L);
        productEntity2.setName("Product2");

        Iterable<ProductEntity> productEntities = Arrays.asList(productEntity1, productEntity2);

        Iterable<Product> products = productMapper.toProducts(productEntities);

        assertThat(products).hasSize(2);
        assertThat(products).extracting("id").containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void testToProductEntity() {
        Product product = new Product();
        product.setId(1L);
        product.setName("SomeProduct");

        ProductEntity productEntity = productMapper.toProductEntity(product);

        assertThat(productEntity).isNotNull();
        assertThat(productEntity.getId()).isEqualTo(product.getId());
        assertThat(productEntity.getName()).isEqualTo(product.getName());
    }

    @Test
    void testToProductEntities() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product2");

        Iterable<Product> products = Arrays.asList(product1, product2);

        Iterable<ProductEntity> productEntities = productMapper.toProductEntities(products);

        assertThat(productEntities).hasSize(2);
        assertThat(productEntities).extracting("id").containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void testToProductRequest() {
        Product product = new Product();
        product.setId(1L);
        product.setName("SomeProduct");

        ProductRequest productRequest = productMapper.toProductRequest(product);

        assertThat(productRequest).isNotNull();
        assertThat(productRequest.getName()).isEqualTo(product.getName());
    }

    @Test
    void testToProductDto() {
        Product product = new Product();
        product.setId(1L);
        product.setName("SomeProduct");

        ProductDto productDto = productMapper.toProductDto(product);

        assertThat(productDto).isNotNull();
        assertThat(productDto.getId()).isEqualTo(product.getId());
        assertThat(productDto.getName()).isEqualTo(product.getName());
    }

    @Test
    void testToProductFromDto() {
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("SomeProduct");

        Product product = productMapper.toProductFromDto(productDto);

        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(productDto.getId());
        assertThat(product.getName()).isEqualTo(productDto.getName());
    }

    @Test
    void testMapLongToProductEntity() {
        Long productId = 1L;

        ProductEntity productEntity = productMapper.map(productId);

        assertThat(productEntity).isNotNull();
        assertThat(productEntity.getId()).isEqualTo(productId);
    }

    @Test
    void testMapProductEntityToLong() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);

        Long productId = productMapper.map(productEntity);

        assertThat(productId).isEqualTo(productEntity.getId());
    }

    @Test
    void testMapNullProductEntityToLong() {
        Long productId = productMapper.map((ProductEntity) null);

        assertThat(productId).isNull();
    }
}
