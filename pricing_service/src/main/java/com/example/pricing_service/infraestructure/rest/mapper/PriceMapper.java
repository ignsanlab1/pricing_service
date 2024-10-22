package com.example.pricing_service.infraestructure.rest.mapper;

import com.example.pricing_service.domain.dto.PriceDto;
import com.example.pricing_service.domain.dto.request.PriceRequest;
import com.example.pricing_service.domain.model.Price;
import com.example.pricing_service.infraestructure.entity.PriceEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {BrandMapper.class, ProductMapper.class})
public interface PriceMapper {

    Price toPrice(PriceEntity priceEntity);

    @InheritInverseConfiguration
    PriceEntity toPriceEntity(Price price);

    PriceDto toPriceDto(PriceEntity priceEntity);

    Price toPriceFromDto(PriceDto priceDto);

    PriceRequest toPriceRequest(Price price);

    PriceDto toPriceDtoFromPrice(Price price);
}