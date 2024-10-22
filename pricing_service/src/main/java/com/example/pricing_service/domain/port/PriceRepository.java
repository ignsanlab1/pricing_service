package com.example.pricing_service.domain.port;

import com.example.pricing_service.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PriceRepository {
    Optional<Price> findApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate);
    Price savePrice(Price price);
    Optional<Price> findPricetoDelete(Long brandId, Long productId, Integer priceList, LocalDateTime startDate,
                              LocalDateTime endDate);
    void deletePrice(Price price);
    List<Price> findAllById(List<Long> priceIds);

    Integer findMaxPriceListByProductAndBrand(Long product, Long brand);
}
