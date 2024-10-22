package com.example.pricing_service.infraestructure.repository;

import com.example.pricing_service.infraestructure.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {
    Optional<PriceEntity> findFirstByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(
            Long productId, Long brandId, LocalDateTime startDate, LocalDateTime endDate);
    Optional<PriceEntity> findByBrandIdAndProductIdAndPriceListAndStartDateAndEndDate(
            Long brandId, Long productId, Integer priceList, LocalDateTime startDate, LocalDateTime endDate);
    @Query("SELECT COALESCE(MAX(p.priceList), 0) FROM PriceEntity p WHERE p.product.id = :productId AND p.brand.id = :brandId")
    Integer findMaxPriceListByProductAndBrand(@Param("productId") Long productId, @Param("brandId") Long brandId);
}