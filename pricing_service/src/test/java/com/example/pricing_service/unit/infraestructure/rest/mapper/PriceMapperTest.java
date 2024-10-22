package com.example.pricing_service.unit.infraestructure.rest.mapper;

import com.example.pricing_service.domain.dto.PriceDto;
import com.example.pricing_service.domain.dto.request.PriceRequest;
import com.example.pricing_service.domain.model.Brand;
import com.example.pricing_service.domain.model.Price;
import com.example.pricing_service.domain.model.Product;
import com.example.pricing_service.infraestructure.entity.BrandEntity;
import com.example.pricing_service.infraestructure.entity.PriceEntity;
import com.example.pricing_service.infraestructure.entity.ProductEntity;
import com.example.pricing_service.infraestructure.rest.mapper.BrandMapper;
import com.example.pricing_service.infraestructure.rest.mapper.PriceMapper;
import com.example.pricing_service.infraestructure.rest.mapper.PriceMapperImpl;
import com.example.pricing_service.infraestructure.rest.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PriceMapperTest {

    @InjectMocks
    private PriceMapperImpl priceMapper;

    @Mock
    private BrandMapper brandMapper;

    @Mock
    private ProductMapper productMapper;

    @Test
    void testToPrice() {
        PriceEntity priceEntity = new PriceEntity();
        priceEntity.setId(1L);
        priceEntity.setPrice(100.0);
        priceEntity.setCurrency("USD");
        priceEntity.setStartDate(LocalDateTime.now());
        priceEntity.setEndDate(LocalDateTime.now().plusDays(1));
        priceEntity.setPriceList(1);
        priceEntity.setPriority(1);
        priceEntity.setBrand(new BrandEntity());
        priceEntity.setProduct(new ProductEntity());

        when(brandMapper.toBrand(any(BrandEntity.class))).thenReturn(new Brand());
        when(productMapper.toProduct(any(ProductEntity.class))).thenReturn(new Product());

        Price price = priceMapper.toPrice(priceEntity);

        assertThat(price).isNotNull();
        assertThat(price.getId()).isEqualTo(priceEntity.getId());
        assertThat(price.getPrice()).isEqualTo(priceEntity.getPrice());
        assertThat(price.getCurrency()).isEqualTo(priceEntity.getCurrency());
        assertThat(price.getStartDate()).isEqualTo(priceEntity.getStartDate());
        assertThat(price.getEndDate()).isEqualTo(priceEntity.getEndDate());
        assertThat(price.getPriceList()).isEqualTo(priceEntity.getPriceList());
        assertThat(price.getPriority()).isEqualTo(priceEntity.getPriority());
    }

    @Test
    void testToPriceEntity() {
        Price price = new Price();
        price.setId(1L);
        price.setPrice(100.0);
        price.setCurrency("USD");
        price.setStartDate(LocalDateTime.now());
        price.setEndDate(LocalDateTime.now().plusDays(1));
        price.setPriceList(1);
        price.setPriority(1);
        price.setBrand(new Brand());
        price.setProduct(new Product());

        when(brandMapper.toBrandEntity(any(Brand.class))).thenReturn(new BrandEntity());
        when(productMapper.toProductEntity(any(Product.class))).thenReturn(new ProductEntity());

        PriceEntity priceEntity = priceMapper.toPriceEntity(price);

        assertThat(priceEntity).isNotNull();
        assertThat(priceEntity.getId()).isEqualTo(price.getId());
        assertThat(priceEntity.getPrice()).isEqualTo(price.getPrice());
        assertThat(priceEntity.getCurrency()).isEqualTo(price.getCurrency());
        assertThat(priceEntity.getStartDate()).isEqualTo(price.getStartDate());
        assertThat(priceEntity.getEndDate()).isEqualTo(price.getEndDate());
        assertThat(priceEntity.getPriceList()).isEqualTo(price.getPriceList());
        assertThat(priceEntity.getPriority()).isEqualTo(price.getPriority());
    }

    @Test
    void testToPriceDto() {
        PriceEntity priceEntity = new PriceEntity();
        priceEntity.setId(1L);
        priceEntity.setBrand(new BrandEntity());
        priceEntity.setProduct(new ProductEntity());
        priceEntity.setPrice(100.0);
        priceEntity.setCurrency("USD");
        priceEntity.setStartDate(LocalDateTime.now());
        priceEntity.setEndDate(LocalDateTime.now().plusDays(1));
        priceEntity.setPriceList(1);
        priceEntity.setPriority(1);

        when(brandMapper.map(any(BrandEntity.class))).thenReturn(1L);
        when(productMapper.map(any(ProductEntity.class))).thenReturn(1L);

        PriceDto priceDto = priceMapper.toPriceDto(priceEntity);

        assertThat(priceDto).isNotNull();
        assertThat(priceDto.getId()).isEqualTo(priceEntity.getId());
        assertThat(priceDto.getBrand()).isNotNull();
        assertThat(priceDto.getProduct()).isNotNull();
        assertThat(priceDto.getPrice()).isEqualTo(priceEntity.getPrice());
        assertThat(priceDto.getCurrency()).isEqualTo(priceEntity.getCurrency());
        assertThat(priceDto.getStartDate()).isEqualTo(priceEntity.getStartDate());
        assertThat(priceDto.getEndDate()).isEqualTo(priceEntity.getEndDate());
        assertThat(priceDto.getPriceList()).isEqualTo(priceEntity.getPriceList());
        assertThat(priceDto.getPriority()).isEqualTo(priceEntity.getPriority());
    }

    @Test
    void testToPriceFromDto() {
        PriceDto priceDto = new PriceDto();
        priceDto.setId(1L);
        priceDto.setBrand(1L);
        priceDto.setProduct(1L);
        priceDto.setPrice(100.0);
        priceDto.setCurrency("USD");
        priceDto.setStartDate(LocalDateTime.now());
        priceDto.setEndDate(LocalDateTime.now().plusDays(1));
        priceDto.setPriceList(1);
        priceDto.setPriority(1);

        BrandEntity brandEntity = BrandEntity.builder()
                .id(1L)
                .build();
        ProductEntity productEntity = ProductEntity.builder()
                .id(1L)
                .build();
        Brand brand = Brand.builder()
                .id(1L)
                .build();
        Product product = Product.builder()
                .id(1L)
                .build();

        when(brandMapper.map(anyLong())).thenReturn(brandEntity);
        when(productMapper.map(anyLong())).thenReturn(productEntity );
        when(brandMapper.toBrand( brandMapper.map( priceDto.getBrand() ) )).thenReturn(brand);
        when(productMapper.toProduct( productMapper.map( priceDto.getProduct() ) )).thenReturn(product);

        Price price = priceMapper.toPriceFromDto(priceDto);

        assertThat(price).isNotNull();
        assertThat(price.getId()).isEqualTo(priceDto.getId());
        assertThat(price.getBrand()).isNotNull();
        assertThat(price.getProduct()).isNotNull();
        assertThat(price.getPrice()).isEqualTo(priceDto.getPrice());
        assertThat(price.getCurrency()).isEqualTo(priceDto.getCurrency());
        assertThat(price.getStartDate()).isEqualTo(priceDto.getStartDate());
        assertThat(price.getEndDate()).isEqualTo(priceDto.getEndDate());
        assertThat(price.getPriceList()).isEqualTo(priceDto.getPriceList());
        assertThat(price.getPriority()).isEqualTo(priceDto.getPriority());
    }
}