package com.chefstable.backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id")
    private BookingEntity booking;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chef_id")
    private ChefEntity chef;

    @Column(nullable = false)
    private int rating;

    @Column(length = 500)
    private String comment;

    @Column(nullable = false)
    private OffsetDateTime reviewDate;

    protected ReviewEntity() {
    }

    public ReviewEntity(String id, BookingEntity booking, ClientEntity client, ChefEntity chef, int rating, String comment, OffsetDateTime reviewDate) {
        this.id = id;
        this.booking = booking;
        this.client = client;
        this.chef = chef;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public String getId() {
        return id;
    }

    public ClientEntity getClient() {
        return client;
    }

    public ChefEntity getChef() {
        return chef;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public OffsetDateTime getReviewDate() {
        return reviewDate;
    }
}
