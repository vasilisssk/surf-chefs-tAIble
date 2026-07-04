package com.chefstable.backend.chefs;

import com.chefstable.backend.chefs.dto.ChefListResponse;
import com.chefstable.backend.chefs.dto.ChefResponse;
import com.chefstable.backend.common.NotFoundException;
import com.chefstable.backend.domain.repository.ChefRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChefService {

    private final ChefRepository chefRepository;
    private final ChefMapper chefMapper;

    public ChefService(ChefRepository chefRepository, ChefMapper chefMapper) {
        this.chefRepository = chefRepository;
        this.chefMapper = chefMapper;
    }

    @Transactional(readOnly = true)
    public ChefListResponse getChefs() {
        return new ChefListResponse(chefRepository.findAll().stream().map(chefMapper::toResponse).toList());
    }

    @Transactional(readOnly = true)
    public ChefResponse getChef(String chefId) {
        return chefRepository.findById(chefId)
                .map(chefMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Chef not found"));
    }
}
