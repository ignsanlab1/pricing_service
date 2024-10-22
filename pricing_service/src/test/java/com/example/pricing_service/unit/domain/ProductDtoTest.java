package com.example.pricing_service.unit.domain;

import com.example.pricing_service.domain.dto.ProductDto;
import com.example.pricing_service.infraestructure.commons.constants.CategoryType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Test 01: Validate that all required fields in ProductDto cannot be null or empty.
     */
    @Test
    public void test01ProductDtoValidation() {
        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .name(null)
                .userId(null)
                .category(null)
                .brandIds(Collections.emptyList())
                .priceIds(null)
                .build();

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
        assertEquals(4, violations.size());

        violations.forEach(v -> {
            String property = v.getPropertyPath().toString();
            String message = v.getMessage();

            switch (property) {
                case "name":
                    assertEquals("Name cannot be null", message);
                    break;
                case "userId":
                    assertEquals("User ID cannot be null", message);
                    break;
                case "category":
                    assertEquals("Category cannot be null", message);
                    break;
                case "brandIds":
                    assertEquals("Brand list cannot be empty", message);
                    break;
            }
        });
    }

    /**
     * Test 02: Validate that a ProductDto with all valid fields does not produce any validation errors.
     */
    @Test
    public void test02ValidProductDto() {
        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .name("Valid Product")
                .userId(1L)
                .category(CategoryType.SHOES)
                .brandIds(List.of(1L, 2L, 3L))
                .priceIds(List.of(1L, 2L))
                .build();

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);
        assertTrue(violations.isEmpty());
    }
}
