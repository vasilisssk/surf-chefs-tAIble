package com.chefstable.backend.rental;

import com.chefstable.backend.domain.entity.RentalPackageEntity;
import com.chefstable.backend.rental.dto.RentalPackageResponse;
import org.springframework.stereotype.Component;

@Component
public class RentalPackageMapper {

    public RentalPackageResponse toResponse(RentalPackageEntity rentalPackage) {
        return new RentalPackageResponse(
                rentalPackage.getId(),
                rentalPackage.getPackageName().name(),
                rentalPackage.getDescription(),
                rentalPackage.getPrice(),
                rentalPackage.getAvailableCount()
        );
    }
}
