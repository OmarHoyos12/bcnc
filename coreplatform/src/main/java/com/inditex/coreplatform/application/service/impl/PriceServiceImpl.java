package com.inditex.coreplatform.application.service.impl;

import com.inditex.coreplatform.application.pricing.PricingStrategy;
import com.inditex.coreplatform.application.service.PriceService;
import com.inditex.coreplatform.domain.model.Price;
import com.inditex.coreplatform.exception.ResourceNotFoundException;
import com.inditex.coreplatform.infrastructure.adapter.persistence.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    private final PricingStrategy pricingStrategy;

    /**
     * Obtiene el precio aplicable para un producto en una fecha específica.
     * @param applicationDate Fecha de aplicación
     * @param productId ID del producto
     * @param brandId ID de la marca
     */
    @Override
    public Price getApplicablePrice(LocalDateTime applicationDate, Long productId, Integer brandId) {
        List<Price> prices = priceRepository.findValidPrices(applicationDate, productId, brandId);
        Price selectedPrice = pricingStrategy.selectPrice(prices);
        if (selectedPrice == null) {
            throw new ResourceNotFoundException("No se encontro precio aplicable para los parametros indicados.");
        }
        return selectedPrice;
    }
}
