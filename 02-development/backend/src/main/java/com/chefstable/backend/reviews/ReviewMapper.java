package com.chefstable.backend.reviews;

import com.chefstable.backend.chefs.ChefMapper;
import com.chefstable.backend.domain.entity.ReviewEntity;
import com.chefstable.backend.profile.ClientMapper;
import com.chefstable.backend.reviews.dto.ReviewResponse;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final ClientMapper clientMapper;
    private final ChefMapper chefMapper;

    public ReviewMapper(ClientMapper clientMapper, ChefMapper chefMapper) {
        this.clientMapper = clientMapper;
        this.chefMapper = chefMapper;
    }

    public ReviewResponse toResponse(ReviewEntity review) {
        return new ReviewResponse(
                review.getId(),
                clientMapper.toResponse(review.getClient()),
                chefMapper.toResponse(review.getChef()),
                review.getRating(),
                review.getComment(),
                review.getReviewDate()
        );
    }
}
