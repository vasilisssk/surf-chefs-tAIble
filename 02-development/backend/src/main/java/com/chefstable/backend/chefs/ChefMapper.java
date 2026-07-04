package com.chefstable.backend.chefs;

import com.chefstable.backend.chefs.dto.ChefResponse;
import com.chefstable.backend.domain.entity.ChefEntity;
import org.springframework.stereotype.Component;

@Component
public class ChefMapper {

    public ChefResponse toResponse(ChefEntity chef) {
        return new ChefResponse(
                chef.getId(),
                chef.getName(),
                chef.getSpecialization(),
                chef.getRating(),
                chef.getTotalReviews(),
                chef.getBio()
        );
    }
}
