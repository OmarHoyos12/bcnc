package com.inditex.coreplatform.application.service;

import com.inditex.coreplatform.domain.model.Price;
import java.time.LocalDateTime;

public interface PriceService {
    Price getApplicablePrice(LocalDateTime applicationDate, Long productId, Integer brandId);
}
