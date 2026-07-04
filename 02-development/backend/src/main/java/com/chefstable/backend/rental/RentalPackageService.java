package com.chefstable.backend.rental;

import com.chefstable.backend.domain.repository.RentalPackageRepository;
import com.chefstable.backend.rental.dto.RentalPackageListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RentalPackageService {

    private final RentalPackageRepository rentalPackageRepository;
    private final RentalPackageMapper rentalPackageMapper;

    public RentalPackageService(RentalPackageRepository rentalPackageRepository, RentalPackageMapper rentalPackageMapper) {
        this.rentalPackageRepository = rentalPackageRepository;
        this.rentalPackageMapper = rentalPackageMapper;
    }

    @Transactional(readOnly = true)
    public RentalPackageListResponse getPackages() {
        return new RentalPackageListResponse(rentalPackageRepository.findAll().stream().map(rentalPackageMapper::toResponse).toList());
    }
}
