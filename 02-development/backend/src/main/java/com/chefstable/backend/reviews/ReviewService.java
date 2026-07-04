package com.chefstable.backend.reviews;

import com.chefstable.backend.common.ConflictException;
import com.chefstable.backend.common.IdGenerator;
import com.chefstable.backend.common.NotFoundException;
import com.chefstable.backend.common.ValidationException;
import com.chefstable.backend.domain.entity.BookingEntity;
import com.chefstable.backend.domain.entity.ChefEntity;
import com.chefstable.backend.domain.entity.ReviewEntity;
import com.chefstable.backend.domain.model.BookingStatus;
import com.chefstable.backend.domain.repository.BookingRepository;
import com.chefstable.backend.domain.repository.ChefRepository;
import com.chefstable.backend.domain.repository.ReviewRepository;
import com.chefstable.backend.reviews.dto.CreateReviewRequest;
import com.chefstable.backend.reviews.dto.ReviewResponse;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final ChefRepository chefRepository;
    private final ReviewMapper reviewMapper;
    private final IdGenerator idGenerator;

    public ReviewService(
            ReviewRepository reviewRepository,
            BookingRepository bookingRepository,
            ChefRepository chefRepository,
            ReviewMapper reviewMapper,
            IdGenerator idGenerator
    ) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
        this.chefRepository = chefRepository;
        this.reviewMapper = reviewMapper;
        this.idGenerator = idGenerator;
    }

    @Transactional
    public ReviewResponse createReview(String clientId, CreateReviewRequest request) {
        if (reviewRepository.existsByBookingId(request.bookingId())) {
            throw new ConflictException("Review already exists for this booking");
        }
        BookingEntity booking = bookingRepository.findDetailedByIdAndClientId(request.bookingId(), clientId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        ChefEntity chef = chefRepository.findById(request.chefId())
                .orElseThrow(() -> new NotFoundException("Chef not found"));
        if (!booking.getCookingClass().getChef().getId().equals(chef.getId())) {
            throw new ValidationException("Chef does not match booking class");
        }
        if (booking.getStatus() != BookingStatus.ATTENDED && booking.getCookingClass().getDateTime().isAfter(OffsetDateTime.now())) {
            throw new ValidationException("Review is available only after the class");
        }
        ReviewEntity review = new ReviewEntity(
                idGenerator.prefixed("review"),
                booking,
                booking.getClient(),
                chef,
                request.rating(),
                request.comment(),
                OffsetDateTime.now()
        );
        chef.applyNewRating(request.rating());
        return reviewMapper.toResponse(reviewRepository.save(review));
    }
}
