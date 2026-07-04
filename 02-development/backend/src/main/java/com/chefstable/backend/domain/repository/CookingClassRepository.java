package com.chefstable.backend.domain.repository;

import com.chefstable.backend.domain.entity.CookingClassEntity;
import jakarta.persistence.LockModeType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CookingClassRepository extends JpaRepository<CookingClassEntity, String> {

    @Query("""
            select c from CookingClassEntity c
            join fetch c.chef
            where c.dateTime >= :from and c.dateTime <= :to
              and (:chefId is null or c.chef.id = :chefId)
              and (:classType is null or c.classType = :classType)
            order by c.dateTime asc
            """)
    List<CookingClassEntity> findAvailableClasses(
            @Param("from") OffsetDateTime from,
            @Param("to") OffsetDateTime to,
            @Param("chefId") String chefId,
            @Param("classType") String classType
    );

    @Query("select c from CookingClassEntity c join fetch c.chef where c.id = :id")
    Optional<CookingClassEntity> findDetailedById(@Param("id") String id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CookingClassEntity c join fetch c.chef where c.id = :id")
    Optional<CookingClassEntity> findByIdForUpdate(@Param("id") String id);
}
