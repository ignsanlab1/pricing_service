package com.example.pricing_service.unit.domain;

import com.example.pricing_service.domain.dto.PriceDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PriceDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Test 01: Validate that all required fields in PriceDto cannot be null.
     */
    @Test
    @Order(1)
    public void test01PriceDtoValidation() {
        PriceDto priceDto = PriceDto.builder()
                .brand(null)
                .product(null)
                .priceList(null)
                .startDate(null)
                .endDate(null)
                .priority(null)
                .price(null)
                .currency(null)
                .build();

        Set<ConstraintViolation<PriceDto>> violations = validator.validate(priceDto);
        assertEquals(9, violations.size());

        violations.forEach(v -> {
            String property = v.getPropertyPath().toString();
            String message = v.getMessage();

            switch (property) {
                case "brandId":
                    assertEquals("Brand ID cannot be null", message);
                    break;
                case "productId":
                    assertEquals("Product ID cannot be null", message);
                    break;
                case "startDate":
                    assertEquals("Start date cannot be null", message);
                    break;
                case "endDate":
                    assertEquals("End date cannot be null", message);
                    break;
                case "priority":
                    assertEquals("Priority cannot be null", message);
                    break;
                case "price":
                    assertEquals("Price cannot be null", message);
                    break;
                case "currency":
                    assertEquals("Currency cannot be null", message);
                    break;
            }
        });
    }

    /**
     * Test 02: Validate that a PriceDto with all valid fields does not produce any validation errors.
     */
    @Test
    @Order(2)
    public void test02ValidPriceDto() {
        PriceDto priceDto = PriceDto.builder()
                .brand(1L)
                .product(1L)
                .priceList(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .priority(1)
                .price(10.0)
                .currency("USD")
                .build();

        Set<ConstraintViolation<PriceDto>> violations = validator.validate(priceDto);
        assertTrue(violations.isEmpty());
    }

    /**
     * Test 03: Validate that endDate cannot be before startDate.
     */
    @Test
    @Order(3)
    public void test03InvalidDateRange() {
        PriceDto priceDto = PriceDto.builder()
                .brand(1L)
                .product(1L)
                .priceList(100)
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now())
                .priority(1)
                .price(10.0)
                .currency("USD")
                .build();

        Set<ConstraintViolation<PriceDto>> violations = validator.validate(priceDto);
        assertEquals(1, violations.size());
        assertEquals("End date must be after start date", violations.iterator().next().getMessage());
    }

    /**
     * Test 04: Validate that the currency must have exactly 3 characters.
     */
    @Test
    @Order(4)
    public void test04InvalidCurrencyCode() {
        PriceDto priceDto = PriceDto.builder()
                .brand(1L)
                .product(1L)
                .priceList(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .priority(1)
                .price(10.0)
                .currency("US")
                .build();

        Set<ConstraintViolation<PriceDto>> violations = validator.validate(priceDto);
        assertEquals(1, violations.size());
        assertEquals("Currency must be a 3-letter ISO code", violations.iterator().next().getMessage());
    }

    /**
     * Test 05: Validate price field when it's set to zero.
     */
    @Test
    @Order(5)
    public void test05PriceZeroValidation() {
        PriceDto priceDto = PriceDto.builder()
                .brand(1L)
                .product(1L)
                .priceList(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .priority(1)
                .price(0.0)
                .currency("USD")
                .build();

        Set<ConstraintViolation<PriceDto>> violations = validator.validate(priceDto);
        assertEquals(1, violations.size());
        assertEquals("Price must be greater than 0", violations.iterator().next().getMessage());
    }

    /**
     * Test 06: Validate negative values for priceList.
     */
    @Test
    @Order(6)
    public void test06NegativePriceListValidation() {
        PriceDto priceDto = PriceDto.builder()
                .brand(1L)
                .product(1L)
                .priceList(-10)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .priority(1)
                .price(10.0)
                .currency("USD")
                .build();

        Set<ConstraintViolation<PriceDto>> violations = validator.validate(priceDto);
        assertEquals(1, violations.size());
        assertEquals("Price list must be greater than or equal to 0", violations.iterator().next().getMessage());
    }

    /**
     * Test 07: Validate negative values for priority.
     */
    @Test
    @Order(7)
    public void test07NegativePriorityValidation() {
        PriceDto priceDto = PriceDto.builder()
                .brand(1L)
                .product(1L)
                .priceList(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .priority(-1)
                .price(10.0)
                .currency("USD")
                .build();

        Set<ConstraintViolation<PriceDto>> violations = validator.validate(priceDto);
        assertEquals(1, violations.size());
        assertEquals("Priority must be greater than or equal to 0", violations.iterator().next().getMessage());
    }
}
