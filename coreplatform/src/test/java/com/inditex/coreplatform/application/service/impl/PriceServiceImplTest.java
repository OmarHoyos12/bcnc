package com.inditex.coreplatform.application.service.impl;

import com.inditex.coreplatform.domain.model.Price;
import com.inditex.coreplatform.exception.ResourceNotFoundException;
import com.inditex.coreplatform.infrastructure.adapter.persistence.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PriceServiceImplTest {

    private PriceRepository priceRepository;
    private PriceServiceImpl priceService;

    @BeforeEach
    void setUp() {
        priceRepository = mock(PriceRepository.class);
        priceService = new PriceServiceImpl(
                priceRepository,
                new com.inditex.coreplatform.application.pricing.HighestPriorityPricingStrategy()
        );
    }

    @Test
    void shouldReturnPromotionalPriceWhenPromotionWindowIsActive() {
        // Se define la hora de aplicación dentro del periodo promocional
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);

        // Construcción de Price usando el patrón Builder
        Price promotionalPrice = Price.builder()
                .id(1L)
                .brandId(1)
                .startDate(LocalDateTime.of(2020, 6, 14, 15, 0))
                .endDate(LocalDateTime.of(2020, 6, 14, 18, 30))
                .priceList(2)
                .productId(35455L)
                .priority(1)
                .price(new BigDecimal("25.45"))
                .currency("EUR")
                .build();

        Price basePrice = Price.builder()
                .id(2L)
                .brandId(1)
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                .priceList(1)
                .productId(35455L)
                .priority(0)
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        // Simula que el repositorio retorna una lista ordenada por prioridad (mayor primero)
        when(priceRepository.findValidPrices(applicationDate, 35455L, 1))
                .thenReturn(List.of(promotionalPrice, basePrice));

        // Se espera obtener el precio promocional
        Price result = priceService.getApplicablePrice(applicationDate, 35455L, 1);
        assertEquals(promotionalPrice, result);
        verify(priceRepository, times(1)).findValidPrices(applicationDate, 35455L, 1);
    }

    @Test
    void shouldThrowResourceNotFoundWhenNoValidPricesAreFound() {
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);

        when(priceRepository.findValidPrices(applicationDate, 35455L, 1))
                .thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> priceService.getApplicablePrice(applicationDate, 35455L, 1));
        verify(priceRepository, times(1)).findValidPrices(applicationDate, 35455L, 1);
    }
}
