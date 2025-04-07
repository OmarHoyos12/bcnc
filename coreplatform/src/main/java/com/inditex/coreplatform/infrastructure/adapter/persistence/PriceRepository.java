package com.inditex.coreplatform.infrastructure.adapter.persistence;

import com.inditex.coreplatform.domain.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    /**
     * Busca precios válidos para los parámetros dados.
     * @param applicationDate Fecha de aplicación
     * @param productId ID del producto
     * @param brandId ID de la marca
     */
    @Query("SELECT p FROM Price p " +
            "WHERE p.productId = :productId " +
            "AND p.brandId = :brandId " +
            "AND :applicationDate BETWEEN p.startDate AND p.endDate " +
            "ORDER BY p.priority DESC, p.endDate DESC")
    List<Price> findValidPrices(@Param("applicationDate") LocalDateTime applicationDate,
                                @Param("productId") Long productId,
                                @Param("brandId") Integer brandId);
}
