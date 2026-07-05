package com.chefstable.backend.domain.repository;

import com.chefstable.backend.domain.entity.BookingEntity;
import com.chefstable.backend.domain.model.BookingStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<BookingEntity, String> {

    @Query("""
            select b from BookingEntity b
            join fetch b.client
            join fetch b.cookingClass c
            join fetch c.chef
            join fetch b.rentalPackage
            where b.client.id = :clientId
            order by c.dateTime desc
            """)
    List<BookingEntity> findDetailedByClientId(@Param("clientId") String clientId);

    @Query("""
            select b from BookingEntity b
            join fetch b.client
            join fetch b.cookingClass c
            join fetch c.chef
            join fetch b.rentalPackage
            where b.id = :id and b.client.id = :clientId
            """)
    Optional<BookingEntity> findDetailedByIdAndClientId(@Param("id") String id, @Param("clientId") String clientId);

    @Query("""
            select b from BookingEntity b
            join fetch b.client
            join fetch b.cookingClass c
            join fetch c.chef
            join fetch b.rentalPackage
            where b.id = :id
            """)
    Optional<BookingEntity> findDetailedById(@Param("id") String id);

    @Query("""
            select b from BookingEntity b
            join fetch b.client
            join fetch b.cookingClass c
            join fetch c.chef
            join fetch b.rentalPackage
            where b.cookingClass.id = :classId and b.status = 'CONFIRMED'
            """)
    List<BookingEntity> findAllConfirmedByClassId(@Param("classId") String classId);

    boolean existsByClientIdAndCookingClassIdAndStatus(String clientId, String classId, BookingStatus status);

    boolean existsByClientIdAndCookingClassId(String clientId, String classId);
}
