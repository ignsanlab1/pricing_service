package com.example.pricing_service.domain.port;

import com.example.pricing_service.domain.dto.BrandDto;
import com.example.pricing_service.domain.dto.request.BrandRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface BrandController {
    ResponseEntity<BrandRequest> getBrandById(String id);
    ResponseEntity<BrandRequest> getBrandByName(String name);
    ResponseEntity<?> createBrand(BrandDto brandDto, HttpServletRequest request);
    ResponseEntity<?> updateBrand(String id, BrandDto brandDto, HttpServletRequest request);
    ResponseEntity<?> deleteBrandById(String id, HttpServletRequest request);
    ResponseEntity<?> deleteBrandByName(String name, HttpServletRequest request);
}
