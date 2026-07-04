package com.chefstable.backend.domain.entity;

import com.chefstable.backend.domain.model.CookingClassStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "cooking_classes")
public class CookingClassEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private OffsetDateTime dateTime;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private int maxParticipants;

    @Column(nullable = false)
    private int availableSeats;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chef_id")
    private ChefEntity chef;

    @Column(nullable = false)
    private String classType;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CookingClassStatus status;

    protected CookingClassEntity() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public int getDuration() {
        return duration;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public ChefEntity getChef() {
        return chef;
    }

    public String getClassType() {
        return classType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CookingClassStatus getStatus() {
        return status;
    }

    public boolean canBeBooked() {
        return status == CookingClassStatus.SCHEDULED && availableSeats > 0;
    }

    public void reserveSeat() {
        if (!canBeBooked()) {
            throw new IllegalStateException("Class has no available seats");
        }
        availableSeats -= 1;
    }

    public void releaseSeat() {
        if (availableSeats < maxParticipants) {
            availableSeats += 1;
        }
    }
}
