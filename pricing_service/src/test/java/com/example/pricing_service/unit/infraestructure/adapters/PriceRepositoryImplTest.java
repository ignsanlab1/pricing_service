package com.example.pricing_service.unit.infraestructure.adapters;

import com.example.pricing_service.domain.model.Brand;
import com.example.pricing_service.domain.model.Price;
import com.example.pricing_service.domain.model.Product;
import com.example.pricing_service.infraestructure.adapters.PriceRepositoryImpl;
import com.example.pricing_service.infraestructure.commons.exceptions.PriceBadRequestException;
import com.example.pricing_service.infraestructure.entity.BrandEntity;
import com.example.pricing_service.infraestructure.entity.PriceEntity;
import com.example.pricing_service.infraestructure.entity.ProductEntity;
import com.example.pricing_service.infraestructure.repository.PriceJpaRepository;
import com.example.pricing_service.infraestructure.rest.mapper.PriceMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PriceRepositoryImplTest {

    @Mock
    private PriceJpaRepository priceJpaRepository;

    @Mock
    private PriceMapper priceMapper;

    @InjectMocks
    private PriceRepositoryImpl priceRepositoryImpl;

    private PriceEntity priceEntity;
    private Price price;

    private Brand brand;
    private Product product;

    private BrandEntity brandEntity;
    private ProductEntity productEntity;

    @BeforeEach
    public void setUp() {
        brand = Brand.builder()
                .id(200L)
                .name("Test Brand")
                .userId(1L)
                .productList(Collections.emptyList())
                .build();

        product = Product.builder()
                .id(100L)
                .name("Test Product")
                .userId(1L)
                .category(null)
                .priceList(Collections.emptyList())
                .build();

        brandEntity = brandEntity.builder()
                .id(100L)
                .name("Test Brand")
                .userId(1L)
                .productList(Collections.emptyList())
                .build();

        productEntity = ProductEntity.builder()
                .id(100L)
                .name("Test Product")
                .userId(1L)
                .category(null)
                .priceList(Collections.emptyList())
                .build();

        priceEntity = PriceEntity.builder()
                .id(1L)
                .product(productEntity)
                .brand(brandEntity)
                .price(9.99)
                .priceList(1)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .build();

        price = Price.builder()
                .id(1L)
                .product(product)
                .brand(brand)
                .price(9.99)
                .priceList(1)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .build();
    }

    @Test
    @Order(1)
    void test01FindApplicablePriceSuccess() {
        when(priceJpaRepository.findFirstByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(
                anyLong(), anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Optional.of(priceEntity));
        when(priceMapper.toPrice(priceEntity)).thenReturn(price);

        LocalDateTime time = LocalDateTime.now();

        Optional<Price> result = priceRepositoryImpl.findApplicablePrice(100L, 200L, time);

        assertTrue(result.isPresent());
        assertEquals(9.99, result.get().getPrice());
        verify(priceJpaRepository).findFirstByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(100L, 200L, time, time);
        verify(priceMapper).toPrice(priceEntity);
    }

    @Test
    @Order(2)
    void test02FindApplicablePriceNotFound() {
        when(priceJpaRepository.findFirstByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(
                anyLong(), anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        LocalDateTime time = LocalDateTime.now();

        Optional<Price> result = priceRepositoryImpl.findApplicablePrice(100L, 200L, time);

        assertFalse(result.isPresent());
        verify(priceJpaRepository).findFirstByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(100L, 200L, time, time);
        /*verify(priceJpaRepository).findFirstByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(
                eq(100L),
                eq(200L),
                argThat(date -> date.isBefore(LocalDateTime.now().plusNanos(1000)) && date.isAfter(LocalDateTime.now().minusNanos(1000))),
                argThat(date -> date.isBefore(LocalDateTime.now().plusNanos(1000)) && date.isAfter(LocalDateTime.now().minusNanos(1000)))
        );*/
        verify(priceMapper, never()).toPrice(any());
    }

    @Test
    @Order(3)
    void test03SavePriceSuccess() {
        when(priceMapper.toPriceEntity(any(Price.class))).thenReturn(priceEntity);
        when(priceJpaRepository.save(any(PriceEntity.class))).thenReturn(priceEntity);
        when(priceMapper.toPrice(priceEntity)).thenReturn(price);

        Price result = priceRepositoryImpl.savePrice(price);

        assertNotNull(result);
        assertEquals(9.99, result.getPrice());
        verify(priceJpaRepository).save(priceEntity);
        verify(priceMapper).toPriceEntity(price);
        verify(priceMapper).toPrice(priceEntity);
    }

    @Test
    @Order(4)
    void test04SavePriceNullThrowsException() {
        Exception exception = assertThrows(PriceBadRequestException.class, () -> {
            priceRepositoryImpl.savePrice(null);
        });

        assertEquals("Error saving the price", exception.getMessage());
        verify(priceJpaRepository, never()).save(any());
    }

    @Test
    @Order(5)
    void test05FindPriceToDeleteSuccess() {
        when(priceJpaRepository.findByBrandIdAndProductIdAndPriceListAndStartDateAndEndDate(
                anyLong(), anyLong(), anyInt(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Optional.of(priceEntity));
        when(priceMapper.toPrice(priceEntity)).thenReturn(price);

        LocalDateTime time = LocalDateTime.now().minusDays(1);
        LocalDateTime time2 = LocalDateTime.now().plusDays(1);

        Optional<Price> result = priceRepositoryImpl.findPricetoDelete(200L, 100L, 1, time, time2);

        assertTrue(result.isPresent());
        assertEquals(9.99, result.get().getPrice());
        verify(priceJpaRepository).findByBrandIdAndProductIdAndPriceListAndStartDateAndEndDate(200L, 100L, 1, time, time2);
        verify(priceMapper).toPrice(priceEntity);
    }

    @Test
    @Order(6)
    void test06FindPriceToDeleteNotFound() {
        when(priceJpaRepository.findByBrandIdAndProductIdAndPriceListAndStartDateAndEndDate(
                anyLong(), anyLong(), anyInt(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        Optional<Price> result = priceRepositoryImpl.findPricetoDelete(200L, 100L, 1, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));

        assertFalse(result.isPresent());
        verify(priceJpaRepository).findByBrandIdAndProductIdAndPriceListAndStartDateAndEndDate(200L, 100L, 1, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        verify(priceMapper, never()).toPrice(any());
    }

    @Test
    @Order(7)
    void test07DeletePriceSuccess() {
        when(priceMapper.toPriceEntity(any(Price.class))).thenReturn(priceEntity);

        priceRepositoryImpl.deletePrice(price);

        verify(priceJpaRepository).delete(priceEntity);
    }

    @Test
    @Order(8)
    void test08DeletePriceNullThrowsException() {
        Exception exception = assertThrows(PriceBadRequestException.class, () -> {
            priceRepositoryImpl.deletePrice(null);
        });

        assertEquals("Error deleting the price", exception.getMessage());
        verify(priceJpaRepository, never()).delete(any());
    }

    @Test
    @Order(9)
    void test09FindAllByIdSuccess() {
        when(priceJpaRepository.findAllById(anyList())).thenReturn(List.of(priceEntity));
        when(priceMapper.toPrice(priceEntity)).thenReturn(price);

        List<Price> result = priceRepositoryImpl.findAllById(List.of(1L));

        assertFalse(result.isEmpty());
        assertEquals(1L, result.get(0).getId());
        verify(priceJpaRepository).findAllById(List.of(1L));
        verify(priceMapper).toPrice(priceEntity);
    }

    @Test
    @Order(10)
    void test10FindAllByIdEmptyList() {
        List<Price> result = priceRepositoryImpl.findAllById(Collections.emptyList());

        assertTrue(result.isEmpty());
        verify(priceJpaRepository, never()).findAllById(anyList());
    }

    @Test
    @Order(11)
    void test11FindMaxPriceListByProductAndBrandSuccess() {
        when(priceJpaRepository.findMaxPriceListByProductAndBrand(100L, 200L)).thenReturn(2);

        Integer result = priceRepositoryImpl.findMaxPriceListByProductAndBrand(100L, 200L);

        assertEquals(2, result);
        verify(priceJpaRepository).findMaxPriceListByProductAndBrand(100L, 200L);
    }
}
