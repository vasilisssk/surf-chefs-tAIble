package com.chefstable.backend.domain.entity;

import com.chefstable.backend.domain.model.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "bookings")
public class BookingEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "class_id")
    private CookingClassEntity cookingClass;

    @Column(nullable = false)
    private OffsetDateTime bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rental_package_id")
    private RentalPackageEntity rentalPackage;

    private OffsetDateTime cancellationDate;

    @Column(nullable = false)
    private int penaltyPoints;

    private String allergies;

    protected BookingEntity() {
    }

    public BookingEntity(String id, ClientEntity client, CookingClassEntity cookingClass, RentalPackageEntity rentalPackage, String allergies, OffsetDateTime bookingDate) {
        this.id = id;
        this.client = client;
        this.cookingClass = cookingClass;
        this.rentalPackage = rentalPackage;
        this.allergies = allergies;
        this.bookingDate = bookingDate;
        this.status = BookingStatus.CONFIRMED;
    }

    public String getId() {
        return id;
    }

    public ClientEntity getClient() {
        return client;
    }

    public CookingClassEntity getCookingClass() {
        return cookingClass;
    }

    public OffsetDateTime getBookingDate() {
        return bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public RentalPackageEntity getRentalPackage() {
        return rentalPackage;
    }

    public OffsetDateTime getCancellationDate() {
        return cancellationDate;
    }

    public int getPenaltyPoints() {
        return penaltyPoints;
    }

    public String getAllergies() {
        return allergies;
    }

    public void cancelByClient(OffsetDateTime now, boolean lateCancellation) {
        status = BookingStatus.CANCELLED_BY_CLIENT;
        cancellationDate = now;
        penaltyPoints = lateCancellation ? 1 : 0;
    }
}
