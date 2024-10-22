package com.example.pricing_service.infraestructure.adapters;

import com.example.pricing_service.domain.model.Price;
import com.example.pricing_service.domain.port.PriceRepository;
import com.example.pricing_service.infraestructure.commons.exceptions.PriceBadRequestException;
import com.example.pricing_service.infraestructure.entity.PriceEntity;
import com.example.pricing_service.infraestructure.repository.PriceJpaRepository;
import com.example.pricing_service.infraestructure.rest.mapper.PriceMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class PriceRepositoryImpl implements PriceRepository {

    private final PriceJpaRepository priceJpaRepository;
    private final PriceMapper priceMapper;

    public PriceRepositoryImpl(PriceJpaRepository priceJpaRepository, PriceMapper priceMapper) {
        this.priceJpaRepository = priceJpaRepository;
        this.priceMapper = priceMapper;
    }

    /**
     * Retrieves the highest-priority price for a product and brand at a given date.
     *
     * @param productId The product's ID.
     * @param brandId The brand's ID.
     * @param applicationDate The date for which the price is needed.
     * @return An {@link Optional} containing the applicable {@link Price}, or empty if no price is found.
     */
    @Override
    public Optional<Price> findApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate) {
        return priceJpaRepository.findFirstByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(
                        productId, brandId, applicationDate, applicationDate)
                .map(priceMapper::toPrice);
    }

    /**
     * Saves a given price in the database.
     *
     * The method transforms the provided {@link Price} domain model into a {@link PriceEntity}
     * suitable for persistence using JPA. After saving the entity, it maps the saved
     * {@link PriceEntity} back to the domain model {@link Price}. If the saving process fails,
     * it throws a {@link PriceBadRequestException}.
     *
     * @param price The {@link Price} domain object to be saved.
     * @return The saved {@link Price} after persistence.
     * @throws PriceBadRequestException if there is an error during the saving process.
     */
    @Override
    public Price savePrice(Price price) {
        return Optional.ofNullable(price)
                .map(priceMapper::toPriceEntity)
                .map(priceJpaRepository::save)
                .map(priceMapper::toPrice)
                .orElseThrow(() -> new PriceBadRequestException("Error saving the price"));
    }

    @Override
    public Optional<Price> findPricetoDelete(Long brandId, Long productId, Integer priceList, LocalDateTime startDate, LocalDateTime endDate) {
        return priceJpaRepository.findByBrandIdAndProductIdAndPriceListAndStartDateAndEndDate(
                brandId, productId, priceList, startDate, endDate)
                .map(priceMapper::toPrice);
    }

    @Override
    public void deletePrice(Price price) {
        Optional.ofNullable(price)
                .map(priceMapper::toPriceEntity)
                .ifPresentOrElse(
                        priceJpaRepository::delete,
                        () -> {
                            throw new PriceBadRequestException("Error deleting the price");
                        }
                );
    }

    /**
     * Finds a list of prices based on their IDs.
     *
     * This method will query the database for the prices that match the provided list of IDs,
     * map the results to the domain model {@link Price}, and return them in a list.
     *
     * @param priceIds List of price IDs to retrieve.
     * @return A list of {@link Price} objects.
     */
    @Override
    public List<Price> findAllById(List<Long> priceIds) {
        if (priceIds == null || priceIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<PriceEntity> priceEntities = priceJpaRepository.findAllById(priceIds);

        return priceEntities.stream()
                .map(priceMapper::toPrice)
                .toList();
    }

    @Override
    public Integer findMaxPriceListByProductAndBrand(Long product, Long brand) {
        return priceJpaRepository.findMaxPriceListByProductAndBrand(product, brand);
    }

}
