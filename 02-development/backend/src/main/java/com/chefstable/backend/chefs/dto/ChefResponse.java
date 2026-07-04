package com.chefstable.backend.chefs.dto;

import java.math.BigDecimal;

public record ChefResponse(
        String id,
        String name,
        String specialization,
        BigDecimal rating,
        int totalReviews,
        String bio
) {
}
