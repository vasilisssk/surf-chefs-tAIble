package com.chefstable.backend.reviews.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateReviewRequest(
        @NotBlank String bookingId,
        @NotBlank String chefId,
        @Min(1) @Max(5) int rating,
        @Size(max = 500) String comment
) {
}
