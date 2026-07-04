package com.chefstable.backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "chefs")
public class ChefEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private BigDecimal rating;

    @Column(nullable = false)
    private int totalReviews;

    private String bio;

    protected ChefEntity() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public String getBio() {
        return bio;
    }

    public void applyNewRating(int ratingValue) {
        BigDecimal total = rating.multiply(BigDecimal.valueOf(totalReviews)).add(BigDecimal.valueOf(ratingValue));
        totalReviews += 1;
        rating = total.divide(BigDecimal.valueOf(totalReviews), 1, java.math.RoundingMode.HALF_UP);
    }
}
