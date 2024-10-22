package com.example.pricing_service.domain.dto;

import com.example.pricing_service.domain.validators.ValidDateRange;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@ValidDateRange
public class PriceDto {

    private Long id;

    @NotNull(message = "Brand ID cannot be null")
    private Long brand;

    @NotNull(message = "Product ID cannot be null")
    private Long product;

    @NotNull(message = "Price list cannot be null")
    @Min(value = 0, message = "Price list must be greater than or equal to 0")
    private Integer priceList;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDateTime endDate;

    @NotNull(message = "Priority cannot be null")
    @Min(value = 0, message = "Priority must be greater than or equal to 0")
    private Integer priority;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Currency cannot be null")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    private String currency;
}
