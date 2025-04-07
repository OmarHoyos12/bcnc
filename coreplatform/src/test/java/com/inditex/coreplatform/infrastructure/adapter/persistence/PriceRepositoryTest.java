package com.inditex.coreplatform.infrastructure.adapter.persistence;

import com.inditex.coreplatform.domain.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PriceRepositoryTest {

    @Autowired
    private PriceRepository priceRepository;

    @BeforeEach
    void setUp() {
        // Limpia el repositorio para evitar datos cargados por data.sql
        priceRepository.deleteAll();
    }
    @Test
    void findValidPricesShouldReturnPricesOrderedByPriorityDesc() {
        // Dados dos precios: uno promocional y uno base
        Price promotionalPrice = Price.builder()
                .brandId(1)
                .startDate(LocalDateTime.of(2020, 6, 14, 15, 0))
                .endDate(LocalDateTime.of(2020, 6, 14, 18, 30))
                .priceList(2)
                .productId(35455L)
                .priority(1) // mayor prioridad
                .price(new BigDecimal("25.45"))
                .currency("EUR")
                .build();

        Price basePrice = Price.builder()
                .brandId(1)
                .startDate(LocalDateTime.of(2020, 6, 14, 0, 0))
                .endDate(LocalDateTime.of(2020, 12, 31, 23, 59))
                .priceList(1)
                .productId(35455L)
                .priority(0) // prioridad menor
                .price(new BigDecimal("35.50"))
                .currency("EUR")
                .build();

        // Se persisten los registros en la base de datos en memoria
        priceRepository.save(promotionalPrice);
        priceRepository.save(basePrice);

        // Cuando se consulta con una fecha dentro de ambos intervalos
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        List<Price> result = priceRepository.findValidPrices(applicationDate, 35455L, 1);

        // Entonces se deben obtener los dos registros y el de mayor prioridad (promotionalPrice) debe aparecer primero
        assertThat(result)
                .isNotEmpty()
                .hasSize(2);
        assertThat(result.get(0).getPriority()).isGreaterThan(result.get(1).getPriority());
    }

    @Test
    void findValidPricesShouldReturnEmptyWhenNoPriceIsValid() {
        // Se inserta un precio cuyo intervalo de validez es en julio
        Price price = Price.builder()
                .brandId(1)
                .startDate(LocalDateTime.of(2020, 7, 1, 0, 0))
                .endDate(LocalDateTime.of(2020, 7, 31, 23, 59))
                .priceList(3)
                .productId(35455L)
                .priority(1)
                .price(new BigDecimal("30.00"))
                .currency("EUR")
                .build();

        priceRepository.save(price);

        // Se consulta con una fecha en junio, que no está dentro del rango del precio insertado
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        List<Price> result = priceRepository.findValidPrices(applicationDate, 35455L, 1);

        // Se espera que la consulta retorne una lista vacía
        assertThat(result).isEmpty();
    }
}
