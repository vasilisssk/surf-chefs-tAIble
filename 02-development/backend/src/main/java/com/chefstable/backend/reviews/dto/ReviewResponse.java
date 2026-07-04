package com.chefstable.backend.reviews.dto;

import com.chefstable.backend.chefs.dto.ChefResponse;
import com.chefstable.backend.profile.dto.ClientResponse;
import java.time.OffsetDateTime;

public record ReviewResponse(
        String id,
        ClientResponse client,
        ChefResponse chef,
        int rating,
        String comment,
        OffsetDateTime reviewDate
) {
}
