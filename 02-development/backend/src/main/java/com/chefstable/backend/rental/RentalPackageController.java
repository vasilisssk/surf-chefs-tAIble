package com.chefstable.backend.rental;

import com.chefstable.backend.rental.dto.RentalPackageListResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rental-packages")
public class RentalPackageController {

    private final RentalPackageService rentalPackageService;

    public RentalPackageController(RentalPackageService rentalPackageService) {
        this.rentalPackageService = rentalPackageService;
    }

    @GetMapping
    RentalPackageListResponse getPackages() {
        return rentalPackageService.getPackages();
    }
}
