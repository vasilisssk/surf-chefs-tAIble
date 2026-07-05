package com.chefstable.backend.bookings.dto;

import com.chefstable.backend.classes.dto.CookingClassResponse;
import com.chefstable.backend.profile.dto.ClientResponse;
import com.chefstable.backend.rental.dto.RentalPackageResponse;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record BookingResponse(
        String id,
        ClientResponse client,
        CookingClassResponse cookingClass,
        OffsetDateTime bookingDate,
        String status,
        RentalPackageResponse rentalPackage,
        OffsetDateTime cancellationDate,
        int penaltyPoints,
        BigDecimal totalPrice
) {
}
