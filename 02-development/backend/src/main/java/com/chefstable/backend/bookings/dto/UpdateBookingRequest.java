package com.chefstable.backend.bookings.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateBookingRequest(
        @NotBlank String action,
        String reason
) {
}
