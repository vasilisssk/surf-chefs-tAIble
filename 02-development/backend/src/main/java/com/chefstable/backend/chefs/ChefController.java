package com.chefstable.backend.chefs;

import com.chefstable.backend.chefs.dto.ChefListResponse;
import com.chefstable.backend.chefs.dto.ChefResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chefs")
public class ChefController {

    private final ChefService chefService;

    public ChefController(ChefService chefService) {
        this.chefService = chefService;
    }

    @GetMapping
    ChefListResponse getChefs() {
        return chefService.getChefs();
    }

    @GetMapping("/{chefId}")
    ChefResponse getChef(@PathVariable String chefId) {
        return chefService.getChef(chefId);
    }
}
