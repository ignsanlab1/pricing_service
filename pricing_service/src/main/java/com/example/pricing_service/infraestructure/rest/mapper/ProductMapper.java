package com.example.pricing_service.infraestructure.rest.mapper;

import com.example.pricing_service.domain.dto.ProductDto;
import com.example.pricing_service.domain.dto.request.ProductRequest;
import com.example.pricing_service.domain.model.Product;
import com.example.pricing_service.infraestructure.entity.ProductEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toProduct(ProductEntity productEntity);

    Iterable<Product> toProducts(Iterable<ProductEntity> productEntities);

    @InheritInverseConfiguration
    ProductEntity toProductEntity(Product product);

    Iterable<ProductEntity> toProductEntities(Iterable<Product> products);

    ProductRequest toProductRequest(Product product);

    ProductDto toProductDto(Product product);

    Product toProductFromDto(ProductDto productDto);

    default ProductEntity map(Long productId) {
        if (productId == null) {
            return null;
        }
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(productId);
        return productEntity;
    }

    default Long map(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }
        return productEntity.getId();
    }
}
