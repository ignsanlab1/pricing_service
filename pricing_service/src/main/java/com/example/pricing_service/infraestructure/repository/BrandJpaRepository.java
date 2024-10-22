package com.example.pricing_service.infraestructure.repository;

import com.example.pricing_service.infraestructure.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandJpaRepository extends JpaRepository<BrandEntity, Long> {
    Optional<BrandEntity> findByName(String name);
    @Modifying
    @Query("DELETE FROM BrandEntity b WHERE b.name = :name")
    void deleteByName(@Param("name") String name);
    @Query("SELECT b FROM BrandEntity b JOIN b.productList p WHERE p.id = :productId")
    List<BrandEntity> findBrandsByProductId(@Param("productId") Long productId);
}