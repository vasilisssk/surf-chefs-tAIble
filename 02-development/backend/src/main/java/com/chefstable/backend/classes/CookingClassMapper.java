package com.chefstable.backend.classes;

import com.chefstable.backend.chefs.ChefMapper;
import com.chefstable.backend.classes.dto.CookingClassResponse;
import com.chefstable.backend.domain.entity.CookingClassEntity;
import org.springframework.stereotype.Component;

@Component
public class CookingClassMapper {

    private final ChefMapper chefMapper;

    public CookingClassMapper(ChefMapper chefMapper) {
        this.chefMapper = chefMapper;
    }

    public CookingClassResponse toResponse(CookingClassEntity cookingClass) {
        return new CookingClassResponse(
                cookingClass.getId(),
                cookingClass.getTitle(),
                cookingClass.getDescription(),
                cookingClass.getDateTime(),
                cookingClass.getDuration(),
                cookingClass.getMaxParticipants(),
                cookingClass.getAvailableSeats(),
                chefMapper.toResponse(cookingClass.getChef()),
                cookingClass.getClassType(),
                cookingClass.getPrice()
        );
    }
}
