package com.chefstable.backend.bookings.dto;

import java.util.List;

public record BookingListResponse(List<BookingResponse> bookings) {
}
