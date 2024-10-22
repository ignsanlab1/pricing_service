package com.example.pricing_service.infraestructure.configuration;

import com.example.pricing_service.infraestructure.commons.constants.CategoryType;
import com.example.pricing_service.infraestructure.entity.BrandEntity;
import com.example.pricing_service.infraestructure.entity.PriceEntity;
import com.example.pricing_service.infraestructure.entity.ProductEntity;
import com.example.pricing_service.infraestructure.repository.BrandJpaRepository;
import com.example.pricing_service.infraestructure.repository.PriceJpaRepository;
import com.example.pricing_service.infraestructure.repository.ProductJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

/*@Configuration
public class DataLoader {

    private final BrandJpaRepository brandJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final PriceJpaRepository priceJpaRepository;

    public DataLoader(BrandJpaRepository brandJpaRepository, ProductJpaRepository productJpaRepository, PriceJpaRepository priceJpaRepository) {
        this.brandJpaRepository = brandJpaRepository;
        this.productJpaRepository = productJpaRepository;
        this.priceJpaRepository = priceJpaRepository;
    }

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {

            ProductEntity product1 = productJpaRepository.save(ProductEntity.builder()
                    .name("Product 1")
                    .userId(1L)
                    .category(CategoryType.SHOES)
                    .build());

            ProductEntity product2 = productJpaRepository.save(ProductEntity.builder()
                    .name("Product 2")
                    .userId(1L)
                    .category(CategoryType.CLOTHES)
                    .build());

            BrandEntity brand1 = brandJpaRepository.save(BrandEntity.builder()
                    .name("Brand 1")
                    .userId(1L)
                    .productList(Arrays.asList(product1, product2))
                    .build());

            BrandEntity brand2 = brandJpaRepository.save(BrandEntity.builder()
                    .name("Brand 2")
                    .userId(2L)
                    .productList(Collections.singletonList(product2))
                    .build());

            priceJpaRepository.save(new PriceEntity(
                    null, brand1.getId(), product1.getId(), 1,
                    LocalDateTime.of(2020, 6, 14, 0, 0),
                    LocalDateTime.of(2020, 12, 31, 23, 59),
                    0, 35.50, "EUR"
            ));

            priceJpaRepository.save(new PriceEntity(
                    null, brand1.getId(), product1.getId(), 2,
                    LocalDateTime.of(2020, 6, 14, 15, 0),
                    LocalDateTime.of(2020, 6, 14, 18, 30),
                    1, 25.45, "EUR"
            ));

            priceJpaRepository.save(new PriceEntity(
                    null, brand2.getId(), product2.getId(), 3,
                    LocalDateTime.of(2020, 6, 15, 0, 0),
                    LocalDateTime.of(2020, 6, 15, 11, 0),
                    1, 30.50, "EUR"
            ));

            priceJpaRepository.save(new PriceEntity(
                    null, brand2.getId(), product2.getId(), 4,
                    LocalDateTime.of(2020, 6, 15, 16, 0),
                    LocalDateTime.of(2020, 12, 31, 23, 59),
                    1, 38.95, "EUR"
            ));
        };
    }
}*/
