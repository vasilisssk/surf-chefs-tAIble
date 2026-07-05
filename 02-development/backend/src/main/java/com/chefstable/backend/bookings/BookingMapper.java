package com.chefstable.backend.bookings;

import com.chefstable.backend.bookings.dto.BookingResponse;
import com.chefstable.backend.classes.CookingClassMapper;
import com.chefstable.backend.domain.entity.BookingEntity;
import com.chefstable.backend.profile.ClientMapper;
import com.chefstable.backend.rental.RentalPackageMapper;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    private final ClientMapper clientMapper;
    private final CookingClassMapper cookingClassMapper;
    private final RentalPackageMapper rentalPackageMapper;

    public BookingMapper(ClientMapper clientMapper, CookingClassMapper cookingClassMapper, RentalPackageMapper rentalPackageMapper) {
        this.clientMapper = clientMapper;
        this.cookingClassMapper = cookingClassMapper;
        this.rentalPackageMapper = rentalPackageMapper;
    }

    public BookingResponse toResponse(BookingEntity booking) {
        return new BookingResponse(
                booking.getId(),
                clientMapper.toResponse(booking.getClient()),
                cookingClassMapper.toResponse(booking.getCookingClass()),
                booking.getBookingDate(),
                booking.getStatus().name(),
                rentalPackageMapper.toResponse(booking.getRentalPackage()),
                booking.getCancellationDate(),
                booking.getPenaltyPoints(),
                booking.getTotalPrice()
        );
    }
}
