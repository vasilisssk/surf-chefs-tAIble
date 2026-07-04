package com.chefstable.backend.classes.dto;

import com.chefstable.backend.chefs.dto.ChefResponse;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CookingClassResponse(
        String id,
        String title,
        String description,
        OffsetDateTime dateTime,
        int duration,
        int maxParticipants,
        int availableSeats,
        ChefResponse chef,
        String classType,
        BigDecimal price
) {
}
