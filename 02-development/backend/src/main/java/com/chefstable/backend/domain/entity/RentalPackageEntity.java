package com.chefstable.backend.domain.entity;

import com.chefstable.backend.domain.model.RentalPackageName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "rental_packages")
public class RentalPackageEntity {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RentalPackageName packageName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int availableCount;

    protected RentalPackageEntity() {
    }

    public String getId() {
        return id;
    }

    public RentalPackageName getPackageName() {
        return packageName;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getAvailableCount() {
        return availableCount;
    }
}
