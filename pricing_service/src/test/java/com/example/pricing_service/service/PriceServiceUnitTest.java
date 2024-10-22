package com.example.pricing_service.service;

import com.example.pricing_service.application.service.PriceService;
import com.example.pricing_service.domain.dto.PriceDto;
import com.example.pricing_service.domain.dto.request.PriceRequest;
import com.example.pricing_service.domain.model.Brand;
import com.example.pricing_service.domain.model.Price;
import com.example.pricing_service.domain.model.Product;
import com.example.pricing_service.domain.port.PriceRepository;
import com.example.pricing_service.infraestructure.commons.constants.CategoryType;
import com.example.pricing_service.infraestructure.commons.exceptions.PriceBadRequestException;
import com.example.pricing_service.infraestructure.commons.exceptions.PriceNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PriceServiceUnitTest {

    @Autowired
    private PriceService priceService;

    @MockBean
    private PriceRepository priceRepository;

    @BeforeEach
    void setUp() {
        when(priceRepository.findApplicablePrice(35455L, 1L, LocalDateTime.of(2020, 6, 14, 10, 0)))
                .thenReturn(Optional.of(Price.builder()
                        .id(1L)
                        .brand(Brand.builder()
                                .id(1L)
                                .name("ZARA")
                                .userId(1001L)
                                .build())
                        .product(Product.builder()
                                .id(35455L)
                                .name("Product Name")
                                .userId(2001L)
                                .category(CategoryType.CLOTHES)
                                .build())
                        .priceList(1)
                        .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                        .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                        .price(35.50)
                        .currency("EUR")
                        .priority(1)
                        .build()));

        when(priceRepository.findApplicablePrice(35455L, 1L, LocalDateTime.of(2020, 6, 14, 16, 0)))
                .thenReturn(Optional.of(Price.builder()
                        .id(2L)
                        .brand(Brand.builder()
                                .id(1L)
                                .name("ZARA")
                                .userId(1001L)
                                .build())
                        .product(Product.builder()
                                .id(35455L)
                                .name("Product Name")
                                .userId(2001L)
                                .category(CategoryType.CLOTHES)
                                .build())
                        .priceList(2)
                        .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                        .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                        .price(25.45)
                        .currency("EUR")
                        .priority(2)
                        .build()));

        when(priceRepository.findApplicablePrice(35455L, 1L, LocalDateTime.of(2020, 6, 16, 21, 0)))
                .thenReturn(Optional.of(Price.builder()
                        .id(4L)
                        .brand(Brand.builder()
                                .id(1L)
                                .name("ZARA")
                                .userId(1001L)
                                .build())
                        .product(Product.builder()
                                .id(35455L)
                                .name("Product Name")
                                .userId(2001L)
                                .category(CategoryType.CLOTHES)
                                .build())
                        .priceList(4)
                        .startDate(LocalDateTime.of(2020, 6, 16, 0, 0))
                        .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                        .price(38.95)
                        .currency("EUR")
                        .priority(1)
                        .build()));

        when(priceRepository.findApplicablePrice(35455L, 1L, LocalDateTime.of(2024, 9, 2, 18, 0)))
                .thenReturn(Optional.empty());
    }

    @Test
    @Order(1)
    public void test01GetApplicablePriceAt10OnJune14() {
        LocalDateTime dateTime1 = LocalDateTime.of(2020, 6, 14, 10, 0);
        PriceRequest applicablePrice1 = priceService.getApplicablePrice(35455L, 1L, dateTime1);
        assertEquals(35.50, applicablePrice1.getPrice());
        assertEquals("EUR", applicablePrice1.getCurrency());
    }

    @Test
    @Order(2)
    public void test02GetApplicablePriceAt16OnJune14() {
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 6, 14, 16, 0);
        PriceRequest applicablePrice2 = priceService.getApplicablePrice(35455L, 1L, dateTime2);
        assertEquals(25.45, applicablePrice2.getPrice());
        assertEquals("EUR", applicablePrice2.getCurrency());
    }

    @Test
    @Order(3)
    public void test03GetApplicablePriceAt21OnJune16() {
        LocalDateTime dateTime3 = LocalDateTime.of(2020, 6, 16, 21, 0);
        PriceRequest applicablePrice3 = priceService.getApplicablePrice(35455L, 1L, dateTime3);
        assertEquals(38.95, applicablePrice3.getPrice());
        assertEquals("EUR", applicablePrice3.getCurrency());
    }

    @Test
    @Order(4)
    public void test04PriceNotFound() {
        LocalDateTime dateTime4 = LocalDateTime.of(2024, 9, 2, 18, 0);
        assertThrows(PriceNotFoundException.class, () -> priceService.getApplicablePrice(35455L, 1L, dateTime4));
    }

    @Test
    @Order(5)
    public void test05BadRequest() {
        LocalDateTime dateTime5 = LocalDateTime.of(2020, 6, 16, 21, 0);
        assertThrows(PriceBadRequestException.class, () -> priceService.getApplicablePrice(-1L, -1L, dateTime5));
    }

    @Test
    @Order(6)
    public void test06CreatePrice() {
        PriceDto priceDto = PriceDto.builder()
                .product(35455L)
                .brand(1L)
                .startDate(LocalDateTime.of(2020, 6, 16, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                .price(42.75)
                .currency("EUR")
                .build();

        when(priceRepository.findMaxPriceListByProductAndBrand(35455L, 1L)).thenReturn(4);

        when(priceRepository.savePrice(any(Price.class))).thenAnswer(invocation -> {
            Price savedPrice = invocation.getArgument(0);
            savedPrice.setId(5L);
            return savedPrice;
        });

        PriceDto createdPrice = priceService.createPrice(priceDto);

        assertNotNull(createdPrice);
        assertEquals(5, createdPrice.getPriceList());
        assertEquals(42.75, createdPrice.getPrice());
        assertEquals("EUR", createdPrice.getCurrency());
    }

    @Test
    @Order(7)
    public void test07DeletePrice() {
        Long brandId = 1L;
        Long productId = 35455L;
        Integer priceList = 1;
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);

        Price priceToDelete = Price.builder()
                .id(1L)
                .brand(Brand.builder().id(brandId).name("ZARA").build())
                .product(Product.builder().id(productId).name("Product Name").build())
                .priceList(priceList)
                .startDate(startDate)
                .endDate(endDate)
                .price(35.50)
                .currency("EUR")
                .build();

        when(priceRepository.findPricetoDelete(brandId, productId, priceList, startDate, endDate))
                .thenReturn(Optional.of(priceToDelete));

        doNothing().when(priceRepository).deletePrice(priceToDelete);

        priceService.deletePrice(brandId, productId, priceList, startDate, endDate);

        verify(priceRepository, times(1)).deletePrice(priceToDelete);
    }
}

