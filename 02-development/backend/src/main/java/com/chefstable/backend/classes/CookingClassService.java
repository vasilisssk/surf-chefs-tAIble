package com.chefstable.backend.classes;

import com.chefstable.backend.classes.dto.CookingClassListResponse;
import com.chefstable.backend.classes.dto.CookingClassResponse;
import com.chefstable.backend.common.NotFoundException;
import com.chefstable.backend.domain.repository.CookingClassRepository;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CookingClassService {

    private final CookingClassRepository cookingClassRepository;
    private final CookingClassMapper cookingClassMapper;

    public CookingClassService(CookingClassRepository cookingClassRepository, CookingClassMapper cookingClassMapper) {
        this.cookingClassRepository = cookingClassRepository;
        this.cookingClassMapper = cookingClassMapper;
    }

    @Transactional(readOnly = true)
    public CookingClassListResponse getClasses(LocalDate dateFrom, LocalDate dateTo, String chefId, String classType) {
        LocalDate from = dateFrom != null ? dateFrom : LocalDate.now();
        LocalDate to = dateTo != null ? dateTo : from.plusDays(7);
        ZoneId zone = ZoneId.systemDefault();
        OffsetDateTime fromDateTime = from.atStartOfDay(zone).toOffsetDateTime();
        OffsetDateTime toDateTime = to.plusDays(1).atStartOfDay(zone).toOffsetDateTime().minusNanos(1);
        return new CookingClassListResponse(cookingClassRepository
                .findAvailableClasses(fromDateTime, toDateTime, chefId, classType)
                .stream()
                .filter(cookingClass -> cookingClass.getAvailableSeats() > 0)
                .map(cookingClassMapper::toResponse)
                .toList());
    }

    @Transactional(readOnly = true)
    public CookingClassResponse getClass(String classId) {
        return cookingClassRepository.findDetailedById(classId)
                .map(cookingClassMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Class not found"));
    }
}
