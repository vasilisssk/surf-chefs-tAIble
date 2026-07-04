package com.chefstable.backend.domain.repository;

import com.chefstable.backend.domain.entity.ChefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChefRepository extends JpaRepository<ChefEntity, String> {
}
