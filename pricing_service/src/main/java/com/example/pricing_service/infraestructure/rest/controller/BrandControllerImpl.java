package com.example.pricing_service.infraestructure.rest.controller;

import com.example.pricing_service.application.service.BrandService;
import com.example.pricing_service.domain.dto.BrandDto;
import com.example.pricing_service.domain.dto.request.BrandRequest;
import com.example.pricing_service.domain.port.BrandController;
import com.example.pricing_service.infraestructure.commons.constants.ApiPathVariables;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.pricing_service.infraestructure.commons.constants.ExceptionMessages.BRAND_NOT_FOUND;
import static com.example.pricing_service.infraestructure.commons.constants.ExceptionMessages.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(ApiPathVariables.V1_ROUTE + ApiPathVariables.BRANDS_ROUTE)
public class BrandControllerImpl implements BrandController {

    private final BrandService brandService;

    public BrandControllerImpl(BrandService brandService) {
        this.brandService = brandService;
    }

    @Override
    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve brand by ID",
            description = "Fetches the details of a brand by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid brand ID"),
            @ApiResponse(responseCode = "404", description = BRAND_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<BrandRequest> getBrandById(@RequestParam String id) {
        BrandRequest brandRequest = brandService.getBrandById(id);
        return ResponseEntity.ok(brandRequest);
    }

    @Override
    @GetMapping("/name")
    @Operation(
            summary = "Retrieve brand by name",
            description = "Fetches the details of a brand by its name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid brand name"),
            @ApiResponse(responseCode = "404", description = BRAND_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<BrandRequest> getBrandByName(@RequestParam String name) {
        BrandRequest brandRequest = brandService.getBrandByName(name);
        return ResponseEntity.ok(brandRequest);
    }

    @Override
    @PostMapping
    @Operation(
            summary = "Create a new brand",
            description = "Creates a new brand with the provided details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Brand created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<?> createBrand(@RequestBody @Valid BrandDto brandDto, HttpServletRequest request) {
        String userId = request.getHeader("userIdRequest");
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (userId == null || jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        BrandDto createBrand = brandService.createBrand(brandDto);
        return ResponseEntity.ok(createBrand);
    }

    @Override
    @PutMapping("/{id}")
    @Operation(
            summary = "Update existing brand",
            description = "Updates the details of an existing brand."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Brand updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = BRAND_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<Void> updateBrand(@PathVariable String id, @RequestBody @Valid BrandDto brandDto, HttpServletRequest request) {
        BrandDto updateBrand = brandService.updateBrand(id, brandDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete brand by ID",
            description = "Deletes a brand by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Brand deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid brand ID"),
            @ApiResponse(responseCode = "404", description = BRAND_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<?> deleteBrandById(@RequestParam String id, HttpServletRequest request) {
        String userId = request.getHeader("userIdRequest");
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (userId == null || jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        this.brandService.deleteBrandById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/name")
    @Operation(
            summary = "Delete brand by name",
            description = "Deletes a brand by its name."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Brand deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid brand name"),
            @ApiResponse(responseCode = "404", description = BRAND_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<?> deleteBrandByName(@RequestParam String name, HttpServletRequest request) {
        String userId = request.getHeader("userIdRequest");
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (userId == null || jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        this.brandService.deleteBrandByName(name);
        return ResponseEntity.noContent().build();
    }
}
