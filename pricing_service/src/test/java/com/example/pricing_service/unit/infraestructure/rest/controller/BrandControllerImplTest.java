package com.example.pricing_service.unit.infraestructure.rest.controller;

import com.example.pricing_service.application.service.BrandService;
import com.example.pricing_service.domain.dto.BrandDto;
import com.example.pricing_service.domain.dto.request.BrandRequest;
import com.example.pricing_service.domain.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BrandControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService brandService;

    private BrandRequest mockBrandRequest;
    private BrandDto mockBrandDto;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockBrandRequest = BrandRequest.builder()
                .name("SomeBrand")
                .productList(Arrays.asList(Product.builder()
                        .id(1L)
                        .name("SomeProduct")
                        .build()))
                .build();

        mockBrandDto = BrandDto.builder()
                .id(1L)
                .name("SomeBrand")
                .userId(1L)
                .productIds(Arrays.asList(1L, 2L, 3L))
                .build();
    }

    @Test
    @Order(1)
    void test01GetBrandById() throws Exception {
        when(brandService.getBrandById("1")).thenReturn(mockBrandRequest);

        mockMvc.perform(get("/v1/brands/id")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(mockBrandRequest.getName()));

        verify(brandService).getBrandById("1");
    }

    @Test
    @Order(2)
    void test02GetBrandByName() throws Exception {
        when(brandService.getBrandByName("SomeBrand")).thenReturn(mockBrandRequest);

        mockMvc.perform(get("/v1/brands/name")
                        .param("name", "SomeBrand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(mockBrandRequest.getName()));

        verify(brandService).getBrandByName("SomeBrand");
    }

    @Test
    @Order(3)
    void test03CreateBrand() throws Exception {
        when(brandService.createBrand(any(BrandDto.class))).thenReturn(mockBrandDto);

        mockMvc.perform(post("/v1/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"SomeBrand\",\"userId\":1,\"productIds\":[1,2,3]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(mockBrandDto.getName()))
                .andExpect(jsonPath("$.id").value(mockBrandDto.getId()));

        verify(brandService).createBrand(any(BrandDto.class));
    }

    @Test
    @Order(4)
    void test04UpdateBrand() throws Exception {
        BrandDto updatedBrandDto = BrandDto.builder()
                .id(1L)
                .name("UpdatedProduct")
                .userId(1L)
                .build();
        when(brandService.updateBrand(eq("1"), any(BrandDto.class))).thenReturn(updatedBrandDto);

        mockMvc.perform(put("/v1/brands/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBrandDto)))
                .andExpect(status().isNoContent());

        verify(brandService).updateBrand(eq("1"), any(BrandDto.class));
    }

    @Test
    @Order(5)
    void test05DeleteBrandById() throws Exception {
        doNothing().when(brandService).deleteBrandById("1");

        mockMvc.perform(delete("/v1/brands/id")
                        .param("id", "1"))
                .andExpect(status().isNoContent());

        verify(brandService).deleteBrandById("1");
    }

    @Test
    @Order(6)
    void test06DeleteBrandByName() throws Exception {
        doNothing().when(brandService).deleteBrandByName("SomeBrand");

        mockMvc.perform(delete("/v1/brands/name")
                        .param("name", "SomeBrand"))
                .andExpect(status().isNoContent());

        verify(brandService).deleteBrandByName("SomeBrand");
    }
}