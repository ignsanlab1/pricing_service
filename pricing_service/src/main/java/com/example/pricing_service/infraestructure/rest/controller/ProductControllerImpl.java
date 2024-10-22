package com.example.pricing_service.infraestructure.rest.controller;

import com.example.pricing_service.application.service.ProductService;
import com.example.pricing_service.domain.dto.ProductDto;
import com.example.pricing_service.domain.dto.request.ProductRequest;
import com.example.pricing_service.domain.port.ProductController;
import com.example.pricing_service.infraestructure.commons.constants.ApiPathVariables;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.pricing_service.infraestructure.commons.constants.ExceptionMessages.INTERNAL_SERVER_ERROR;
import static com.example.pricing_service.infraestructure.commons.constants.ExceptionMessages.PRODUCT_NOT_FOUND;

@RestController
@RequestMapping(ApiPathVariables.V1_ROUTE + ApiPathVariables.PRODUCTS_ROUTE)
public class ProductControllerImpl implements ProductController {
    private ProductService productService;

    public ProductControllerImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve product by ID",
            description = "Fetches the details of a product by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product ID"),
            @ApiResponse(responseCode = "404", description = PRODUCT_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<ProductRequest> getProductById(String id) {
        ProductRequest productRequest = productService.getProductById(id);
        return ResponseEntity.ok(productRequest);
    }

    @Override
    @PostMapping
    @Operation(
            summary = "Create a new product",
            description = "Creates a new product with the provided details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<ProductDto> createProduct(ProductDto productDto) {
        ProductDto createProduct = productService.createProduct(productDto);
        return ResponseEntity.ok(createProduct);
    }

    @Override
    @PutMapping("/{id}")
    @Operation(
            summary = "Update existing product",
            description = "Updates the details of an existing product."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = PRODUCT_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<Void> updateProduct(@PathVariable String id, @RequestBody @Valid ProductDto productDto) {
        ProductDto updateProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete product by ID",
            description = "Deletes a product by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product ID"),
            @ApiResponse(responseCode = "404", description = PRODUCT_NOT_FOUND),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR)
    })
    public ResponseEntity<Void> deleteProductById(String id) {
        this.productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
