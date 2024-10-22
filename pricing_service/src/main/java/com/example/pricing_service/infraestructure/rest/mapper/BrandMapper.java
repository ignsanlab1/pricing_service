package com.example.pricing_service.infraestructure.rest.mapper;

import com.example.pricing_service.domain.dto.BrandDto;
import com.example.pricing_service.domain.dto.request.BrandRequest;
import com.example.pricing_service.domain.model.Brand;
import com.example.pricing_service.infraestructure.entity.BrandEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    Brand toBrand(BrandEntity brandEntity);

    Iterable<Brand> toBrands(Iterable<BrandEntity> brandEntities);

    @InheritInverseConfiguration
    BrandEntity toBrandEntity(Brand brand);

    BrandRequest toBrandRequest(Brand brand);

    BrandDto toBrandDto(Brand brand);

    Brand toBrandFromDto(BrandDto brandDto);

    default BrandEntity map(Long brandId) {
        if (brandId == null) {
            return null;
        }
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(brandId);
        return brandEntity;
    }

    default Long map(BrandEntity brandEntity) {
        if (brandEntity == null) {
            return null;
        }
        return brandEntity.getId();
    }
}
