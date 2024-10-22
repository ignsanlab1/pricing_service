package com.example.pricing_service.domain.validators;

import com.example.pricing_service.domain.dto.PriceDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, PriceDto> {

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(PriceDto priceDto, ConstraintValidatorContext context) {
        if (priceDto == null) {
            return true;
        }
        return priceDto.getEndDate() != null &&
                priceDto.getStartDate() != null &&
                priceDto.getEndDate().isAfter(priceDto.getStartDate());
    }
}
