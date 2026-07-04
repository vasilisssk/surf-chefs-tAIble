package com.chefstable.backend.domain.repository;

import com.chefstable.backend.domain.entity.ClientEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntity, String> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<ClientEntity> findByEmail(String email);

    Optional<ClientEntity> findByPhone(String phone);
}
