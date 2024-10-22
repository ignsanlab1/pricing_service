package com.example.pricing_service.unit.infraestructure.rest.controller;

import com.example.pricing_service.application.service.ProductService;
import com.example.pricing_service.domain.dto.ProductDto;
import com.example.pricing_service.domain.dto.request.ProductRequest;
import com.example.pricing_service.domain.model.Price;
import com.example.pricing_service.infraestructure.commons.constants.CategoryType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductRequest mockProductRequest;
    private ProductDto mockProductDto;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Price mockPrice = Price.builder()
                .id(1L)
                .price(99.99)
                .currency("USD")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .build();

        mockProductRequest = ProductRequest.builder()
                .name("SomeProduct")
                .category(CategoryType.CLOTHES)
                .priceList(List.of(mockPrice))
                .build();

        mockProductDto = ProductDto.builder()
                .id(1L)
                .name("SomeProduct")
                .userId(123L)
                .category(CategoryType.CLOTHES)
                .brandIds(List.of(1L, 2L))
                .priceIds(List.of(1L))
                .build();
    }

    @Test
    @Order(1)
    void test01GetProductById() throws Exception {
        when(productService.getProductById("1")).thenReturn(mockProductRequest);

        mockMvc.perform(get("/v1/products/id")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(mockProductRequest.getName()))
                .andExpect(jsonPath("$.category").value(mockProductRequest.getCategory().name()))
                .andExpect(jsonPath("$.priceList[0].price").value(mockProductRequest.getPriceList().get(0).getPrice()))
                .andExpect(jsonPath("$.priceList[0].currency").value(mockProductRequest.getPriceList().get(0).getCurrency()));

        verify(productService).getProductById("1");
    }

    @Test
    @Order(2)
    void test02CreateProduct() throws Exception {
        when(productService.createProduct(any(ProductDto.class))).thenReturn(mockProductDto);

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProductDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(mockProductDto.getName()))
                .andExpect(jsonPath("$.category").value(mockProductDto.getCategory().name()))
                .andExpect(jsonPath("$.brandIds[0]").value(mockProductDto.getBrandIds().get(0)))
                .andExpect(jsonPath("$.priceIds[0]").value(mockProductDto.getPriceIds().get(0)));

        verify(productService).createProduct(any(ProductDto.class));
    }

    @Test
    @Order(3)
    void test03UpdateProduct() throws Exception {
        ProductDto updatedProductDto = ProductDto.builder()
                .id(1L)
                .name("UpdatedProduct")
                .userId(1L)
                .category(CategoryType.CLOTHES)
                .brandIds(List.of(1L, 2L))
                .priceIds(List.of(1L))
                .build();

        when(productService.updateProduct(eq("1"), any(ProductDto.class))).thenReturn(updatedProductDto);

        mockMvc.perform(put("/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProductDto)))
                .andExpect(status().isNoContent());

        verify(productService).updateProduct(eq("1"), any(ProductDto.class));
    }

    @Test
    @Order(4)
    void test04DeleteProductById() throws Exception {
        doNothing().when(productService).deleteProductById("1");

        mockMvc.perform(delete("/v1/products/id")
                        .param("id", "1"))
                .andExpect(status().isNoContent());

        verify(productService).deleteProductById("1");
    }
}