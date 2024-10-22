package com.example.pricing_service.application.service;

import com.example.pricing_service.domain.dto.PriceDto;
import com.example.pricing_service.domain.dto.request.PriceRequest;
import com.example.pricing_service.domain.model.Price;
import com.example.pricing_service.infraestructure.entity.PriceEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceService {
    PriceRequest getApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate);
    PriceDto createPrice(PriceDto priceDto);
    void deletePrice(Long productId, Long brandId, Integer priceList, LocalDateTime startDate, LocalDateTime endDate);
    List<Price> findPricesByIds(List<Long> priceIds);
}
