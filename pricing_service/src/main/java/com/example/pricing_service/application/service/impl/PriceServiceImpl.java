package com.example.pricing_service.application.service.impl;

import com.example.pricing_service.application.validation.ValidationResult;
import com.example.pricing_service.domain.dto.PriceDto;
import com.example.pricing_service.domain.dto.request.PriceRequest;
import com.example.pricing_service.application.service.PriceService;
import com.example.pricing_service.domain.model.Price;
import com.example.pricing_service.infraestructure.commons.exceptions.PriceBadRequestException;
import com.example.pricing_service.infraestructure.commons.exceptions.PriceNotFoundException;
import com.example.pricing_service.domain.port.PriceRepository;
import com.example.pricing_service.infraestructure.rest.mapper.PriceMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.pricing_service.infraestructure.commons.constants.ExceptionMessages.PRICE_BAD_REQUEST;
import static com.example.pricing_service.infraestructure.commons.constants.ExceptionMessages.PRICE_NOT_FOUND;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    private final PriceMapper priceMapper;

    public PriceServiceImpl(PriceRepository priceRepository, PriceMapper priceMapper) {
        this.priceRepository = priceRepository;
        this.priceMapper = priceMapper;
    }

    /**
     * Fetches the applicable price for a given product and brand on a specific date.
     *
     * @param productId The ID of the product.
     * @param brandId The ID of the brand.
     * @param applicationDate The date to find the applicable price for.
     * @return A {@link PriceRequest} with the price details.
     * @throws PriceNotFoundException if no price is found.
     */
    @Override
    public PriceRequest getApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate) {
        return validateParameters(productId, brandId, applicationDate)
                .filter(ValidationResult::valid)
                .map(result -> fetchPrice(productId, brandId, applicationDate)
                        .orElseThrow(() -> new PriceNotFoundException(PRICE_NOT_FOUND)))
                .orElseThrow(() -> new PriceBadRequestException(PRICE_BAD_REQUEST));
    }

    @Override
    @Transactional
    public PriceDto createPrice(PriceDto priceDto) {
        Integer lastPriceList = priceRepository.findMaxPriceListByProductAndBrand(priceDto.getProduct(), priceDto.getBrand());

        priceDto.setPriceList(lastPriceList + 1);
        return Optional.of(priceDto)
                .map(priceMapper::toPriceFromDto)
                .map(priceRepository::savePrice)
                .map(priceMapper::toPriceDtoFromPrice)
                .orElseThrow(() -> new PriceBadRequestException("Error saving a price"));
    }

    @Override
    @Transactional
    public void deletePrice(Long brandId, Long productId, Integer priceList, LocalDateTime startDate, LocalDateTime endDate) {
        findPrice(brandId, productId, priceList, startDate, endDate)
                .ifPresentOrElse(
                        priceRepository::deletePrice,
                        () -> {
                            throw new PriceNotFoundException("Price not found for the given criteria");
                        }
                );
    }

    /**
     * Find PriceEntity entities based on a list of IDs.
     * @param priceIds List of Price IDs.
     * @return PriceEntity list corresponding to the IDs provided.
     */
    public List<Price> findPricesByIds(List<Long> priceIds) {
        if (priceIds == null || priceIds.isEmpty()) {
            return Collections.emptyList();
        }

        return priceRepository.findAllById(priceIds);
    }

    private Optional<Price> findPrice(Long brandId, Long productId, Integer priceList, LocalDateTime startDate, LocalDateTime endDate) {
        return priceRepository.findPricetoDelete(brandId, productId, priceList, startDate, endDate);
    }


    /**
     * Validates the input parameters to ensure they are not null and have valid values.
     *
     * @param productId The ID of the product.
     * @param brandId The ID of the brand.
     * @param applicationDate The date to find the applicable price for.
     * @return An {@link Optional} containing a {@link ValidationResult} indicating if the parameters are valid or not.
     */
    private Optional<ValidationResult> validateParameters(Long productId, Long brandId, LocalDateTime applicationDate) {
        if (productId == null || productId <= 0L || brandId == null || brandId <= 0L || applicationDate == null) {
            return Optional.of(new ValidationResult(false, "Invalid input parameters"));
        }
        return Optional.of(new ValidationResult(true, ""));
    }

    /**
     * Retrieves the applicable price from the repository based on the given parameters.
     *
     * @param productId The ID of the product.
     * @param brandId The ID of the brand.
     * @param applicationDate The date to find the applicable price for.
     * @return An {@link Optional} containing a {@link PriceRequest} if a price is found, or empty if not.
     */
    private Optional<PriceRequest> fetchPrice(Long productId, Long brandId, LocalDateTime applicationDate) {
        return priceRepository.findApplicablePrice(productId, brandId, applicationDate)
                .map(priceMapper::toPriceRequest);
    }

}
