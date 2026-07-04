package com.chefstable.backend.bookings.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBookingRequest(
        @NotBlank String classId,
        @NotBlank String rentalPackageId,
        String allergies
) {
}
