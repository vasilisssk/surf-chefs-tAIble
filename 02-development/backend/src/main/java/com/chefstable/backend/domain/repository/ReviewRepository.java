package com.chefstable.backend.domain.repository;

import com.chefstable.backend.domain.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, String> {

    boolean existsByBookingId(String bookingId);
}
