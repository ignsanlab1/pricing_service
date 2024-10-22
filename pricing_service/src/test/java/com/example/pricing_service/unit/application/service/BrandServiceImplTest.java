package com.example.pricing_service.unit.application.service;

import com.example.pricing_service.application.service.impl.BrandServiceImpl;
import com.example.pricing_service.domain.dto.BrandDto;
import com.example.pricing_service.domain.dto.request.BrandRequest;
import com.example.pricing_service.domain.model.Brand;
import com.example.pricing_service.domain.model.Product;
import com.example.pricing_service.domain.port.BrandRepository;
import com.example.pricing_service.domain.port.ProductRepository;
import com.example.pricing_service.infraestructure.commons.exceptions.BrandNotFoundException;
import com.example.pricing_service.infraestructure.rest.mapper.BrandMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {

    @InjectMocks
    private BrandServiceImpl brandService;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandMapper brandMapper;

    private BrandRequest mockBrandRequest;
    private BrandDto mockBrandDto;
    private Brand mockBrand;

    @BeforeEach
    void setUp() {

        mockBrandRequest = BrandRequest.builder()
                .name("SomeBrand")
                .productList(Arrays.asList(Product.builder()
                        .id(1L)
                        .name("SomeProduct")
                        .build()))
                .build();

        mockBrandDto = BrandDto.builder()
                .id(1L)
                .name("SomeBrand")
                .userId(1L)
                .productIds(Arrays.asList(1L, 2L, 3L))
                .build();

        mockBrand = Brand.builder()
                .id(1L)
                .name("SomeBrand")
                .userId(1L)
                .productList(new ArrayList<>())
                .build();
    }

    @Test
    @Order(1)
    void test01GetBrandById_Success() {
        when(brandRepository.findBrandById("1")).thenReturn(Optional.of(mockBrand));
        when(brandMapper.toBrandRequest(mockBrand)).thenReturn(mockBrandRequest);

        BrandRequest result = brandService.getBrandById("1");

        assertThat(result).isEqualTo(mockBrandRequest);
        verify(brandRepository).findBrandById("1");
        verify(brandMapper).toBrandRequest(mockBrand);
    }

    @Test
    @Order(2)
    void test02GetBrandById_NotFound() {
        when(brandRepository.findBrandById("1")).thenReturn(Optional.empty());

        assertThatExceptionOfType(BrandNotFoundException.class)
                .isThrownBy(() -> brandService.getBrandById("1"))
                .withMessageContaining("with ID: 1");

        verify(brandRepository).findBrandById("1");
    }

    @Test
    @Order(3)
    void test03GetBrandByName_Success() {
        when(brandRepository.findBrandByName("SomeBrand")).thenReturn(Optional.of(mockBrand));
        when(brandMapper.toBrandRequest(mockBrand)).thenReturn(mockBrandRequest);

        BrandRequest result = brandService.getBrandByName("SomeBrand");

        assertThat(result).isEqualTo(mockBrandRequest);
        verify(brandRepository).findBrandByName("SomeBrand");
        verify(brandMapper).toBrandRequest(mockBrand);
    }

    @Test
    @Order(4)
    void test04GetBrandByName_NotFound() {
        when(brandRepository.findBrandByName("SomeBrand")).thenReturn(Optional.empty());

        assertThatExceptionOfType(BrandNotFoundException.class)
                .isThrownBy(() -> brandService.getBrandByName("SomeBrand"))
                .withMessageContaining("with name: SomeBrand");

        verify(brandRepository).findBrandByName("SomeBrand");
    }

    @Test
    @Order(5)
    void test05CreateBrand() {
        when(brandMapper.toBrandFromDto(mockBrandDto)).thenReturn(mockBrand);
        when(brandRepository.saveBrand(any(Brand.class))).thenReturn(mockBrand);
        when(brandMapper.toBrandDto(mockBrand)).thenReturn(mockBrandDto);
        when(productRepository.findAllById(mockBrandDto.getProductIds())).thenReturn(Arrays.asList(new Product()));

        BrandDto result = brandService.createBrand(mockBrandDto);

        assertThat(result).isEqualTo(mockBrandDto);
        verify(brandMapper).toBrandFromDto(mockBrandDto);
        verify(brandRepository).saveBrand(mockBrand);
    }

    @Test
    @Order(6)
    void test06UpdateBrand_Success() {
        when(brandRepository.findBrandById("1")).thenReturn(Optional.of(mockBrand));
        when(brandMapper.toBrandDto(mockBrand)).thenReturn(mockBrandDto);
        when(productRepository.findAllById(mockBrandDto.getProductIds())).thenReturn(Arrays.asList(new Product()));
        when(brandRepository.saveBrand(any(Brand.class))).thenReturn(mockBrand);

        BrandDto result = brandService.updateBrand("1", mockBrandDto);

        assertThat(result).isEqualTo(mockBrandDto);
        verify(brandRepository).findBrandById("1");
        verify(brandRepository).saveBrand(mockBrand);
    }

    @Test
    @Order(7)
    void test07UpdateBrand_NotFound() {
        when(brandRepository.findBrandById("1")).thenReturn(Optional.empty());

        assertThatExceptionOfType(BrandNotFoundException.class)
                .isThrownBy(() -> brandService.updateBrand("1", mockBrandDto))
                .withMessageContaining("Error updating Brand with ID 1");

        verify(brandRepository).findBrandById("1");
    }

    @Test
    @Order(8)
    void test08DeleteBrandById_Success() {
        when(brandRepository.findBrandById("1")).thenReturn(Optional.of(mockBrand));
        when(brandMapper.toBrandRequest(mockBrand)).thenReturn(mockBrandRequest);

        brandService.deleteBrandById("1");

        verify(brandRepository).deleteById(anyLong());
    }

    @Test
    @Order(9)
    void test09DeleteBrandById_NotFound() {
        when(brandRepository.findBrandById("1")).thenReturn(Optional.empty());

        assertThatExceptionOfType(BrandNotFoundException.class)
                .isThrownBy(() -> brandService.deleteBrandById("1"))
                .withMessageContaining("Error deleting Brand with ID 1");

        verify(brandRepository).findBrandById("1");
    }

    @Test
    @Order(10)
    void test10DeleteBrandByName_Success() {
        when(brandRepository.findBrandByName("SomeBrand")).thenReturn(Optional.of(mockBrand));
        when(brandMapper.toBrandRequest(mockBrand)).thenReturn(mockBrandRequest);

        brandService.deleteBrandByName("SomeBrand");

        verify(brandRepository).deleteByName("SomeBrand");
    }

    @Test
    @Order(11)
    void test11DeleteBrandByName_NotFound() {
        when(brandRepository.findBrandByName("SomeBrand")).thenReturn(Optional.empty());

        assertThatExceptionOfType(BrandNotFoundException.class)
                .isThrownBy(() -> brandService.deleteBrandByName("SomeBrand"))
                .withMessageContaining("Error deleting Brand with name SomeBrand");

        verify(brandRepository).findBrandByName("SomeBrand");
    }
}
