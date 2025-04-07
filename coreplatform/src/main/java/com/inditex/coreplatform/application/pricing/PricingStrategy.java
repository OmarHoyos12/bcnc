package com.inditex.coreplatform.application.pricing;

import com.inditex.coreplatform.domain.model.Price;
import java.util.List;

public interface PricingStrategy {
    Price selectPrice(List<Price> validPrices);
}
