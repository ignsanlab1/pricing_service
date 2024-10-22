package com.example.pricing_service.service;

import com.example.pricing_service.infraestructure.commons.exceptions.PriceBadRequestException;
import com.example.pricing_service.infraestructure.commons.exceptions.PriceNotFoundException;
import com.example.pricing_service.infraestructure.entity.BrandEntity;
import com.example.pricing_service.infraestructure.entity.ProductEntity;
import com.example.pricing_service.infraestructure.repository.PriceJpaRepository;
import com.example.pricing_service.domain.dto.request.PriceRequest;
import com.example.pricing_service.application.service.PriceService;
import com.example.pricing_service.infraestructure.entity.PriceEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PriceServiceTest {

    @Autowired
    private PriceService priceService;

    @Autowired
    private PriceJpaRepository priceJpaRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1L);
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(35455L);
        priceJpaRepository.save(new PriceEntity(
                null, brandEntity, productEntity , 1,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59),
                0, 35.50, "EUR"
        ));
        priceJpaRepository.save(new PriceEntity(
                null, brandEntity, productEntity , 2,
                LocalDateTime.of(2020, 6, 14, 15, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30),
                1, 25.45, "EUR"
        ));
        priceJpaRepository.save(new PriceEntity(
                null, brandEntity, productEntity , 3,
                LocalDateTime.of(2020, 6, 15, 0, 0),
                LocalDateTime.of(2020, 6, 15, 11, 0),
                1, 30.50, "EUR"
        ));
        priceJpaRepository.save(new PriceEntity(
                null, brandEntity, productEntity , 4,
                LocalDateTime.of(2020, 6, 15, 16, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59),
                1, 38.95, "EUR"
        ));
    }


    /**
     * Test 1: Request at 10:00 on June 14th for product 35455 and brand 1 (ZARA)
     * Verifies that the correct price is returned for the given date and time.
     */
    @Test
    @Order(1)
    public void test01GetApplicablePriceAt10OnJune14() {
        LocalDateTime dateTime1 = LocalDateTime.of(2020, 6, 14, 10, 0);
        PriceRequest applicablePrice1 = priceService.getApplicablePrice(35455L, 1L, dateTime1);
        assertEquals(35.50, applicablePrice1.getPrice());
    }

    /**
     * Test 2: Request at 16:00 on June 14th for product 35455 and brand 1 (ZARA)
     * Verifies that the correct price is returned for the given date and time.
     */
    @Test
    @Order(2)
    public void test02GetApplicablePriceAt16OnJune14() {
        LocalDateTime dateTime2 = LocalDateTime.of(2020, 6, 14, 16, 0);
        PriceRequest applicablePrice2 = priceService.getApplicablePrice(35455L, 1L, dateTime2);
        assertEquals(25.45, applicablePrice2.getPrice());
    }

    /**
     * Test 3: Request at 21:00 on June 14th for product 35455 and brand 1 (ZARA)
     * Verifies that the correct price is returned for the given date and time.
     */
    @Test
    @Order(3)
    public void test03GetApplicablePriceAt21OnJune14() {
        LocalDateTime dateTime3 = LocalDateTime.of(2020, 6, 14, 21, 0);
        PriceRequest applicablePrice3 = priceService.getApplicablePrice(35455L, 1L, dateTime3);
        assertEquals(35.50, applicablePrice3.getPrice());
    }

    /**
     * Test 4: Request at 10:00 on June 15th for product 35455 and brand 1 (ZARA)
     * Verifies that the correct price is returned for the given date and time.
     */
    @Test
    @Order(4)
    public void test04GetApplicablePriceAt10OnJune15() {
        LocalDateTime dateTime4 = LocalDateTime.of(2020, 6, 15, 10, 0);
        PriceRequest applicablePrice4 = priceService.getApplicablePrice(35455L, 1L, dateTime4);
        assertEquals(30.50, applicablePrice4.getPrice());
    }

    /**
     * Test 5: Request at 21:00 on June 16th for product 35455 and brand 1 (ZARA)
     * Verifies that the correct price is returned for the given date and time.
     */
    @Test
    @Order(5)
    public void test05GetApplicablePriceAt21OnJune16() {
        LocalDateTime dateTime5 = LocalDateTime.of(2020, 6, 16, 21, 0);
        PriceRequest applicablePrice5 = priceService.getApplicablePrice(35455L, 1L, dateTime5);
        assertEquals(38.95, applicablePrice5.getPrice());
    }

    /**
     * Test 06: Request with bad parameters - Bad Request
     * Verifies that a Bad Request exception is thrown when providing incorrect parameters.
     */
    @Test
    @Order(6)
    public void test06BadRequest() {
        LocalDateTime dateTime6 = LocalDateTime.of(2020, 6, 16, 21, 0);
        assertThrows(PriceBadRequestException.class, () -> priceService.getApplicablePrice(-1L, -1L, dateTime6));
    }

    /**
     * Test 07: Request for non-existent product - Not Found
     * Verifies that a Not Found exception is thrown when the requested product is not found.
     */
    @Test
    @Order(7)
    public void test07NotFound() {
        LocalDateTime dateTime7 = LocalDateTime.of(2024, 9, 2, 18, 0);
        assertThrows(PriceNotFoundException.class, () -> priceService.getApplicablePrice(35455L, 1L, dateTime7));
    }
}

