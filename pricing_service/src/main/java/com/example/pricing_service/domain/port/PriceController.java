package com.example.pricing_service.domain.port;

import com.example.pricing_service.domain.dto.PriceDto;
import com.example.pricing_service.domain.dto.request.PriceRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface PriceController {
    ResponseEntity<PriceRequest> getPrice(Long productId, Long brandId, LocalDateTime applicationDate);
    ResponseEntity<PriceDto> createPrice(PriceDto priceDto);
    ResponseEntity<Void> deletePrice(Long productId, Long brandId, Integer priceList, LocalDateTime startDate, LocalDateTime endDate);
}
