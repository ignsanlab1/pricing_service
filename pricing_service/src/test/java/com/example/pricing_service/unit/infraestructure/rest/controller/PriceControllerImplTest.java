package com.example.pricing_service.unit.infraestructure.rest.controller;

import com.example.pricing_service.application.service.PriceService;
import com.example.pricing_service.domain.dto.PriceDto;
import com.example.pricing_service.domain.dto.request.PriceRequest;
import com.example.pricing_service.infraestructure.commons.exceptions.PriceBadRequestException;
import com.example.pricing_service.infraestructure.commons.exceptions.PriceNotFoundException;
import com.example.pricing_service.infraestructure.rest.controller.PriceControllerImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static com.example.pricing_service.infraestructure.commons.constants.ExceptionMessages.PRICE_BAD_REQUEST;
import static com.example.pricing_service.infraestructure.commons.constants.ExceptionMessages.PRICE_NOT_FOUND;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceControllerImpl.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PriceControllerImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PriceService priceService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(priceService.getApplicablePrice(any(Long.class), any(Long.class), any(LocalDateTime.class)))
                .thenAnswer(invocation -> {
                    Long productId = invocation.getArgument(0);
                    Long brandId = invocation.getArgument(1);
                    LocalDateTime applicationDate = invocation.getArgument(2);

                    if (applicationDate.equals(LocalDateTime.of(2020, 6, 14, 10, 0))) {
                        return new PriceRequest(productId, brandId, 1, LocalDateTime.of(2020, 6, 14, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59), 35.50, "EUR");
                    } else if (applicationDate.equals(LocalDateTime.of(2020, 6, 14, 16, 0))) {
                        return new PriceRequest(productId, brandId, 2, LocalDateTime.of(2020, 6, 14, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59), 25.45, "EUR");
                    } else if (applicationDate.equals(LocalDateTime.of(2020, 6, 14, 21, 0))) {
                        return new PriceRequest(productId, brandId, 1, LocalDateTime.of(2020, 6, 14, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59), 35.50, "EUR");
                    } else if (applicationDate.equals(LocalDateTime.of(2020, 6, 15, 10, 0))) {
                        return new PriceRequest(productId, brandId, 3, LocalDateTime.of(2020, 6, 15, 0, 0),
                                LocalDateTime.of(2020, 6, 15, 11, 0), 30.50, "EUR");
                    } else if (applicationDate.equals(LocalDateTime.of(2020, 6, 16, 21, 0))) {
                        return new PriceRequest(productId, brandId, 4, LocalDateTime.of(2020, 6, 16, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59), 38.95, "EUR");
                    } else if (applicationDate.equals(LocalDateTime.of(2020, 6, 17, 10, 0))) {
                        throw new PriceNotFoundException(PRICE_NOT_FOUND);
                    } else {
                        throw new PriceBadRequestException(PRICE_BAD_REQUEST);
                    }
                });
        when(priceService.createPrice(any(PriceDto.class))).thenAnswer(invocation -> {
            PriceDto priceDto = invocation.getArgument(0);
            return priceDto;
        });

        doNothing().when(priceService).deletePrice(anyLong(), anyLong(), anyInt(), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    /**
     * Test 01: Makes a request at 10:00 on June 14th for product 35455 and brand 1 (ZARA)
     * Verifies that the response status is OK and the price is 35.50 EUR.
     */
    @Test
    @Order(1)
    void test01GetApplicablePriceAt10OnJune14() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/prices")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    /**
     * Test 02: Makes a request at 16:00 on June 14th for product 35455 and brand 1 (ZARA)
     * Verifies that the response status is OK and the price is 35.50 EUR.
     */
    @Test
    @Order(2)
    void test02GetApplicablePriceAt16OnJune14() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/prices")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(25.45))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    /**
     * Test 03: Makes a request at 21:00 on June 14th for product 35455 and brand 1 (ZARA)
     * Verifies that the response status is OK and the price is 35.50 EUR.
     */
    @Test
    @Order(3)
    void test03GetApplicablePriceAt21OnJune14() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/prices")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-14T21:00:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    /**
     * Test 04: Makes a request at 10:00 on June 15th for product 35455 and brand 1 (ZARA)
     * Verifies that the response status is OK and the price is 30.50 EUR.
     */
    @Test
    @Order(4)
    void test04GetApplicablePriceAt10OnJune15() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/prices")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-15T10:00:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(30.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    /**
     * Test 05: Makes a request at 21:00 on June 16th for product 35455 and brand 1 (ZARA)
     * Verifies that the response status is OK and the price is 38.95 EUR.
     */
    @Test
    @Order(5)
    void test05GetApplicablePriceAt21OnJune16() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/prices")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-16T21:00:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(38.95))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    /**
     * Test 06: Request with bad parameters - Bad Request
     * Verifies that a Bad Request exception is thrown when providing incorrect parameters.
     */
    @Test
    @Order(6)
    void test06GetPriceInvalidInputParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/prices")
                        .param("productId", "-1")
                        .param("brandId", "-1")
                        .param("applicationDate", "2020-06-18T10:00:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test 07: Request for non-existent product - Not Found
     * Verifies that a Not Found exception is thrown when the requested product is not found.
     */
    @Test
    @Order(7)
    void test07GetPriceNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/prices")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-17T10:00:00")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /**
     * Test 08: Creates a new price for a product and brand
     * Verifies that the created price is returned with status OK.
     */
    @Test
    @Order(8)
    void test08CreatePrice() throws Exception {
        PriceDto priceDto = new PriceDto();
        priceDto.setProduct(35455L);
        priceDto.setBrand(1L);
        priceDto.setPriceList(1);
        priceDto.setStartDate(LocalDateTime.of(2020, 6, 14, 0, 0));
        priceDto.setEndDate(LocalDateTime.of(2020, 12, 31, 23, 59));
        priceDto.setPrice(35.50);
        priceDto.setCurrency("EUR");
        priceDto.setPriority(1);

        when(priceService.createPrice(any(PriceDto.class))).thenReturn(priceDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(priceDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    /**
     * Test 09: Attempts to create a price with bad request parameters.
     * Verifies that a Bad Request status is returned.
     */
    @Test
    @Order(9)
    void test09CreatePriceBadRequest() throws Exception {
        PriceDto priceDto = new PriceDto();
        priceDto.setProduct(null);

        when(priceService.createPrice(any(PriceDto.class))).thenThrow(new PriceBadRequestException("Invalid data"));

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(priceDto)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test 10: Deletes a price for a product and brand
     * Verifies that the response status is No Content after deletion.
     */
    @Test
    @Order(10)
    void test10DeletePrice() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("priceList", "1")
                        .param("startDate", "2020-06-14T00:00:00")
                        .param("endDate", "2020-12-31T23:59:59"))
                .andExpect(status().isNoContent());

        verify(priceService).deletePrice(1L, 35455L, 1,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59));
    }

    /**
     * Test 11: Attempts to delete a price with non-existent parameters
     * Verifies that a Not Found status is returned.
     */
    @Test
    @Order(11)
    void test11DeletePriceNotFound() throws Exception {
        doThrow(new PriceNotFoundException("Price not found"))
                .when(priceService).deletePrice(anyLong(), anyLong(), anyInt(), any(LocalDateTime.class), any(LocalDateTime.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/prices")
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("priceList", "1")
                        .param("startDate", "2020-06-14T00:00:00")
                        .param("endDate", "2020-12-31T23:59:59"))
                .andExpect(status().isNotFound());
    }
}
