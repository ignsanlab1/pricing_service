package com.example.pricing_service.unit.application.service;

import com.example.pricing_service.application.service.impl.PriceServiceImpl;
import com.example.pricing_service.domain.dto.PriceDto;
import com.example.pricing_service.domain.dto.request.PriceRequest;
import com.example.pricing_service.domain.model.Brand;
import com.example.pricing_service.domain.model.Price;
import com.example.pricing_service.domain.model.Product;
import com.example.pricing_service.domain.port.PriceRepository;
import com.example.pricing_service.infraestructure.commons.constants.CategoryType;
import com.example.pricing_service.infraestructure.commons.exceptions.PriceBadRequestException;
import com.example.pricing_service.infraestructure.commons.exceptions.PriceNotFoundException;
import com.example.pricing_service.infraestructure.rest.mapper.PriceMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class PriceServiceImplTest {

    @InjectMocks
    private PriceServiceImpl priceService;

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private PriceMapper priceMapper;

    private Price mockPrice;
    private PriceDto mockPriceDto;
    private PriceRequest mockPriceRequest;
    private LocalDateTime mockApplicationDate;
    private Brand mockBrand;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        mockBrand = Brand.builder()
                .id(1L)
                .userId(123L)
                .name("SomeBrand")
                .build();

        mockProduct = Product.builder()
                .name("SomeProduct")
                .category(CategoryType.CLOTHES)
                .priceList(Collections.emptyList())
                .build();

        mockApplicationDate = LocalDateTime.now();
        mockPrice = Price.builder()
                .id(1L)
                .price(100.0)
                .currency("USD")
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .priceList(1)
                .product(mockProduct)
                .brand(mockBrand)
                .build();

        mockPriceDto = PriceDto.builder()
                .brand(1L)
                .product(1L)
                .price(100.0)
                .currency("USD")
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .priceList(1)
                .product(mockProduct.getId())
                .brand(mockBrand.getId())
                .build();

        mockPriceRequest = PriceRequest.builder()
                .productId(1L)
                .brandId(1L)
                .price(100.0)
                .currency("USD")
                .startDate(mockApplicationDate.minusDays(1))
                .endDate(mockApplicationDate.plusDays(1))
                .priceList(1)
                .productId(mockProduct.getId())
                .brandId(mockBrand.getId())
                .build();
    }

    @Test
    @Order(1)
    void test01GetApplicablePrice_Success() {
        Long productId = 1L;
        Long brandId = 1L;

        when(priceRepository.findApplicablePrice(productId, brandId, mockApplicationDate))
                .thenReturn(Optional.of(mockPrice));
        when(priceMapper.toPriceRequest(mockPrice)).thenReturn(mockPriceRequest);

        PriceRequest result = priceService.getApplicablePrice(productId, brandId, mockApplicationDate);

        assertNotNull(result);
        assertEquals(mockPriceRequest, result);

        verify(priceRepository).findApplicablePrice(productId, brandId, mockApplicationDate);
        verify(priceMapper).toPriceRequest(mockPrice);
    }

    @Test
    @Order(2)
    void tes02tGetApplicablePrice_PriceNotFoundException() {
        Long productId = 1L;
        Long brandId = 1L;

        when(priceRepository.findApplicablePrice(productId, brandId, mockApplicationDate))
                .thenReturn(Optional.empty());

        assertThrows(PriceNotFoundException.class, () -> {
            priceService.getApplicablePrice(productId, brandId, mockApplicationDate);
        });

        verify(priceRepository).findApplicablePrice(productId, brandId, mockApplicationDate);
        verify(priceMapper, never()).toPriceRequest(any());
    }

    @Test
    @Order(3)
    void test03GetApplicablePrice_PriceBadRequestException() {
        Long invalidProductId = -1L;

        assertThrows(PriceBadRequestException.class, () -> {
            priceService.getApplicablePrice(invalidProductId, 1L, mockApplicationDate);
        });

        verify(priceRepository, never()).findApplicablePrice(anyLong(), anyLong(), any());
    }

    @Test
    @Order(4)
    void test04CreatePrice_Success() {
        Integer lastPriceList = 2;
        Price savedPrice = new Price();

        when(priceRepository.findMaxPriceListByProductAndBrand(mockPriceDto.getProduct(), mockPriceDto.getBrand()))
                .thenReturn(lastPriceList);
        when(priceMapper.toPriceFromDto(mockPriceDto)).thenReturn(mockPrice);
        when(priceRepository.savePrice(mockPrice)).thenReturn(savedPrice);
        when(priceMapper.toPriceDtoFromPrice(savedPrice)).thenReturn(mockPriceDto);

        PriceDto result = priceService.createPrice(mockPriceDto);

        assertNotNull(result);
        assertEquals(mockPriceDto, result);

        verify(priceRepository).findMaxPriceListByProductAndBrand(mockPriceDto.getProduct(), mockPriceDto.getBrand());
        verify(priceRepository).savePrice(mockPrice);
    }

    @Test
    @Order(5)
    void test05CreatePrice_PriceBadRequestException() {
        Integer lastPriceList = 2;
        when(priceRepository.findMaxPriceListByProductAndBrand(mockPriceDto.getProduct(), mockPriceDto.getBrand()))
                .thenReturn(lastPriceList);
        when(priceMapper.toPriceFromDto(mockPriceDto)).thenReturn(mockPrice);
        when(priceRepository.savePrice(any())).thenThrow(new PriceBadRequestException("Error saving a price"));

        assertThrows(PriceBadRequestException.class, () -> {
            priceService.createPrice(mockPriceDto);
        });

        verify(priceRepository).savePrice(any());
    }

    @Test
    @Order(6)
    void test06DeletePrice_Success() {
        Long brandId = 1L;
        Long productId = 1L;
        Integer priceList = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        when(priceRepository.findPricetoDelete(brandId, productId, priceList, startDate, endDate))
                .thenReturn(Optional.of(mockPrice));

        priceService.deletePrice(brandId, productId, priceList, startDate, endDate);

        verify(priceRepository).deletePrice(mockPrice);
    }

    @Test
    @Order(7)
    void test07DeletePrice_PriceNotFoundException() {
        Long brandId = 1L;
        Long productId = 1L;
        Integer priceList = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        when(priceRepository.findPricetoDelete(brandId, productId, priceList, startDate, endDate))
                .thenReturn(Optional.empty());

        assertThrows(PriceNotFoundException.class, () -> {
            priceService.deletePrice(brandId, productId, priceList, startDate, endDate);
        });

        verify(priceRepository, never()).deletePrice(any());
    }

    @Test
    @Order(8)
    void test08FindPricesByIds_Success() {
        List<Long> priceIds = Arrays.asList(1L, 2L, 3L);
        List<Price> prices = Arrays.asList(new Price(), new Price());

        when(priceRepository.findAllById(priceIds)).thenReturn(prices);

        List<Price> result = priceService.findPricesByIds(priceIds);

        assertEquals(prices, result);
        verify(priceRepository).findAllById(priceIds);
    }

    @Test
    @Order(9)
    void test09FindPricesByIds_EmptyList() {
        List<Price> result = priceService.findPricesByIds(null);

        assertTrue(result.isEmpty());

        verify(priceRepository, never()).findAllById(any());
    }
}
