package com.example.pricing_service.unit.infraestructure.rest.mapper;

import com.example.pricing_service.domain.dto.BrandDto;
import com.example.pricing_service.domain.dto.request.BrandRequest;
import com.example.pricing_service.domain.model.Brand;
import com.example.pricing_service.infraestructure.entity.BrandEntity;
import com.example.pricing_service.infraestructure.rest.mapper.BrandMapper;
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
class BrandMapperTest {

    private BrandMapper brandMapper;

    @BeforeEach
    void setUp() {
        brandMapper = Mappers.getMapper(BrandMapper.class);
    }

    @Test
    void testToBrand() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1L);
        brandEntity.setName("SomeBrand");

        Brand brand = brandMapper.toBrand(brandEntity);

        assertThat(brand).isNotNull();
        assertThat(brand.getId()).isEqualTo(brandEntity.getId());
        assertThat(brand.getName()).isEqualTo(brandEntity.getName());
    }

    @Test
    void testToBrands() {
        BrandEntity brandEntity1 = new BrandEntity();
        brandEntity1.setId(1L);
        brandEntity1.setName("Brand1");

        BrandEntity brandEntity2 = new BrandEntity();
        brandEntity2.setId(2L);
        brandEntity2.setName("Brand2");

        Iterable<BrandEntity> brandEntities = Arrays.asList(brandEntity1, brandEntity2);

        Iterable<Brand> brands = brandMapper.toBrands(brandEntities);

        assertThat(brands).hasSize(2);
        assertThat(brands).extracting("id").containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void testToBrandEntity() {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("SomeBrand");

        BrandEntity brandEntity = brandMapper.toBrandEntity(brand);

        assertThat(brandEntity).isNotNull();
        assertThat(brandEntity.getId()).isEqualTo(brand.getId());
        assertThat(brandEntity.getName()).isEqualTo(brand.getName());
    }

    @Test
    void testToBrandRequest() {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("SomeBrand");

        BrandRequest brandRequest = brandMapper.toBrandRequest(brand);

        assertThat(brandRequest).isNotNull();
        assertThat(brandRequest.getName()).isEqualTo(brand.getName());
    }

    @Test
    void testToBrandDto() {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("SomeBrand");

        BrandDto brandDto = brandMapper.toBrandDto(brand);

        assertThat(brandDto).isNotNull();
        assertThat(brandDto.getId()).isEqualTo(brand.getId());
        assertThat(brandDto.getName()).isEqualTo(brand.getName());
    }

    @Test
    void testToBrandFromDto() {
        BrandDto brandDto = new BrandDto();
        brandDto.setId(1L);
        brandDto.setName("SomeBrand");

        Brand brand = brandMapper.toBrandFromDto(brandDto);

        assertThat(brand).isNotNull();
        assertThat(brand.getId()).isEqualTo(brandDto.getId());
        assertThat(brand.getName()).isEqualTo(brandDto.getName());
    }

    @Test
    void testMapLongToBrandEntity() {
        Long brandId = 1L;

        BrandEntity brandEntity = brandMapper.map(brandId);

        assertThat(brandEntity).isNotNull();
        assertThat(brandEntity.getId()).isEqualTo(brandId);
    }

    @Test
    void testMapBrandEntityToLong() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1L);

        Long brandId = brandMapper.map(brandEntity);

        assertThat(brandId).isEqualTo(brandEntity.getId());
    }

    @Test
    void testMapNullBrandEntityToLong() {
        Long brandId = brandMapper.map((BrandEntity) null);

        assertThat(brandId).isNull();
    }
}
