package com.chefstable.backend.rental.dto;

import java.math.BigDecimal;

public record RentalPackageResponse(
        String id,
        String packageName,
        String description,
        BigDecimal price,
        int availableCount
) {
}
