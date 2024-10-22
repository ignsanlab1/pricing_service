package com.example.pricing_service.infraestructure.repository;

import com.example.pricing_service.infraestructure.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByHashCode(int hashCode);
    ProductEntity findByName(String name);
}
