package com.chefstable.backend.reviews;

import com.chefstable.backend.common.CurrentClient;
import com.chefstable.backend.reviews.dto.CreateReviewRequest;
import com.chefstable.backend.reviews.dto.ReviewResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final CurrentClient currentClient;

    public ReviewController(ReviewService reviewService, CurrentClient currentClient) {
        this.reviewService = reviewService;
        this.currentClient = currentClient;
    }

    @PostMapping
    ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(currentClient.id(), request));
    }
}
