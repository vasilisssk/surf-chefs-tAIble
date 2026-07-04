package com.chefstable.backend.domain.repository;

import com.chefstable.backend.domain.entity.RentalPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalPackageRepository extends JpaRepository<RentalPackageEntity, String> {
}
