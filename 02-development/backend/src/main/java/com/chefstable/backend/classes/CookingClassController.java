package com.chefstable.backend.classes;

import com.chefstable.backend.classes.dto.CookingClassListResponse;
import com.chefstable.backend.classes.dto.CookingClassResponse;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/classes")
public class CookingClassController {

    private final CookingClassService cookingClassService;

    public CookingClassController(CookingClassService cookingClassService) {
        this.cookingClassService = cookingClassService;
    }

    @GetMapping
    CookingClassListResponse getClasses(
            @RequestParam(name = "date_from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(name = "date_to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(name = "chef_id", required = false) String chefId,
            @RequestParam(name = "class_type", required = false) String classType
    ) {
        return cookingClassService.getClasses(dateFrom, dateTo, chefId, classType);
    }

    @GetMapping("/{classId}")
    CookingClassResponse getClass(@PathVariable String classId) {
        return cookingClassService.getClass(classId);
    }
}
