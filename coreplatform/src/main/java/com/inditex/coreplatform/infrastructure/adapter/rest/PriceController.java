package com.inditex.coreplatform.infrastructure.adapter.rest;

import com.inditex.coreplatform.application.service.PriceService;
import com.inditex.coreplatform.infrastructure.adapter.rest.dto.PriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    /**
     * Endpoint para consultar el precio aplicable.
     * @param applicationDate Fecha en formato yyyy-MM-dd'T'HH:mm:ss
     * @param productId ID del producto
     * @param brandId ID de la marca
     */
    @GetMapping
    public ResponseEntity<PriceResponse> getPrice(
            @RequestParam("applicationDate")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime applicationDate,
            @RequestParam("productId") Long productId,
            @RequestParam("brandId") Integer brandId) {
        return ResponseEntity.ok(new PriceResponse(priceService.getApplicablePrice(applicationDate, productId, brandId)));
    }
}
