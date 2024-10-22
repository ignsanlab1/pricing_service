package com.example.pricing_service.unit.domain;

import com.example.pricing_service.domain.dto.BrandDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BrandDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testBrandDtoValidation() {
        BrandDto brandDto = BrandDto.builder()
                .name(null)
                .userId(null)
                .productIds(null)
                .build();

        Set<ConstraintViolation<BrandDto>> violations = validator.validate(brandDto);

        assertEquals(2, violations.size());

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("userId")));
    }

    @Test
    public void testValidBrandDto() {
        BrandDto brandDto = BrandDto.builder()
                .name("Brand A")
                .userId(1L)
                .productIds(List.of(1L, 2L))
                .build();

        Set<ConstraintViolation<BrandDto>> violations = validator.validate(brandDto);
        assertTrue(violations.isEmpty());
    }
}
