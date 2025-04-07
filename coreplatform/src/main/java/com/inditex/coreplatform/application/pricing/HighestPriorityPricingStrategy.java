package com.inditex.coreplatform.application.pricing;

import com.inditex.coreplatform.domain.model.Price;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class HighestPriorityPricingStrategy implements PricingStrategy {

    /**
     * Selecciona el primer precio de la lista (el de mayor prioridad).
     * @param validPrices Lista de precios válidos ordenados
     */
    @Override
    public Price selectPrice(List<Price> validPrices) {
        return validPrices.isEmpty() ? null : validPrices.get(0);
    }
}