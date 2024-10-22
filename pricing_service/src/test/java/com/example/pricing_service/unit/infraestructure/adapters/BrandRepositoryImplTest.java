package com.example.pricing_service.unit.infraestructure.adapters;

import com.example.pricing_service.domain.model.Brand;
import com.example.pricing_service.infraestructure.adapters.BrandRepositoryImpl;
import com.example.pricing_service.infraestructure.commons.exceptions.BrandBadRequestException;
import com.example.pricing_service.infraestructure.entity.BrandEntity;
import com.example.pricing_service.infraestructure.repository.BrandJpaRepository;
import com.example.pricing_service.infraestructure.rest.mapper.BrandMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BrandRepositoryImplTest {

    @Mock
    private BrandJpaRepository brandJpaRepository;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandRepositoryImpl brandRepositoryImpl;

    private BrandEntity brandEntity;
    private Brand brand;

    @BeforeEach
    public void setUp() {
        brandEntity = BrandEntity.builder()
                .id(1L)
                .name("Test Brand")
                .userId(100L)
                .build();

        brand = Brand.builder()
                .id(1L)
                .name("Test Brand")
                .userId(100L)
                .build();
    }

    @Test
    @Order(1)
    void test01FindBrandByNameSuccess() {
        when(brandJpaRepository.findByName("Test Brand")).thenReturn(Optional.of(brandEntity));
        when(brandMapper.toBrand(brandEntity)).thenReturn(brand);

        Optional<Brand> result = brandRepositoryImpl.findBrandByName("Test Brand");

        assertTrue(result.isPresent());
        assertEquals("Test Brand", result.get().getName());
        verify(brandJpaRepository).findByName("Test Brand");
        verify(brandMapper).toBrand(brandEntity);
    }

    @Test
    @Order(2)
    void test02FindBrandByNameNotFound() {
        when(brandJpaRepository.findByName(anyString())).thenReturn(Optional.empty());

        Optional<Brand> result = brandRepositoryImpl.findBrandByName("Non-Existent Brand");

        assertFalse(result.isPresent());
        verify(brandJpaRepository).findByName(anyString());
        verify(brandMapper, never()).toBrand(any());
    }

    @Test
    @Order(3)
    void test03FindBrandByIdSuccess() {
        when(brandJpaRepository.findById(1L)).thenReturn(Optional.of(brandEntity));
        when(brandMapper.toBrand(brandEntity)).thenReturn(brand);

        Optional<Brand> result = brandRepositoryImpl.findBrandById("1");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(brandJpaRepository).findById(1L);
        verify(brandMapper).toBrand(brandEntity);
    }

    @Test
    @Order(4)
    void test04SaveBrandSuccess() {
        when(brandMapper.toBrandEntity(any(Brand.class))).thenReturn(brandEntity);
        when(brandJpaRepository.save(any(BrandEntity.class))).thenReturn(brandEntity);
        when(brandMapper.toBrand(brandEntity)).thenReturn(brand);

        Brand result = brandRepositoryImpl.saveBrand(brand);

        assertNotNull(result);
        assertEquals(brand.getId(), result.getId());
        verify(brandJpaRepository).save(brandEntity);
        verify(brandMapper).toBrandEntity(brand);
        verify(brandMapper).toBrand(brandEntity);
    }

    @Test
    @Order(5)
    void test05SaveBrandNullThrowsException() {
        Exception exception = assertThrows(BrandBadRequestException.class, () -> {
            brandRepositoryImpl.saveBrand(null);
        });

        assertEquals("Error saving the brand", exception.getMessage());
        verify(brandJpaRepository, never()).save(any());
    }

    @Test
    @Order(6)
    void test06DeleteByIdSuccess() {
        brandRepositoryImpl.deleteById(1L);

        verify(brandJpaRepository).deleteById(1L);
    }

    @Test
    @Order(7)
    void test07DeleteByNameSuccess() {
        brandRepositoryImpl.deleteByName("Test Brand");

        verify(brandJpaRepository).deleteByName("Test Brand");
    }

    @Test
    @Order(8)
    void test08FindAllByIdSuccess() {
        when(brandJpaRepository.findAllById(List.of(1L))).thenReturn(List.of(brandEntity));
        when(brandMapper.toBrand(brandEntity)).thenReturn(brand);

        List<Brand> result = brandRepositoryImpl.findAllById(List.of(1L));

        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).getId());
        verify(brandJpaRepository).findAllById(List.of(1L));
        verify(brandMapper).toBrand(brandEntity);
    }

    @Test
    @Order(9)
    void test09FindBrandsByProductIdSuccess() {
        when(brandJpaRepository.findBrandsByProductId(1L)).thenReturn(List.of(brandEntity));
        when(brandMapper.toBrand(brandEntity)).thenReturn(brand);

        List<Brand> result = brandRepositoryImpl.findBrandsByProductId(1L);

        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).getId());
        verify(brandJpaRepository).findBrandsByProductId(1L);
        verify(brandMapper).toBrand(brandEntity);
    }
}
