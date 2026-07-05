package com.chefstable.backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "clients")
public class ClientEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private OffsetDateTime registrationDate;

    @Column(nullable = false)
    private int totalClassesAttended;

    private String allergies;

    @Column(name = "is_permanent_client", nullable = false)
    private boolean permanentClient;

    @Column(name = "refresh_token")
    private String refreshToken;

    protected ClientEntity() {
    }

    public ClientEntity(String id, String firstName, String email, String phone, String passwordHash, OffsetDateTime registrationDate) {
        this.id = id;
        this.firstName = firstName;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.registrationDate = registrationDate;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public OffsetDateTime getRegistrationDate() {
        return registrationDate;
    }

    public int getTotalClassesAttended() {
        return totalClassesAttended;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public boolean isPermanentClient() {
        return permanentClient;
    }

    public void refreshPermanentStatus() {
        this.permanentClient = totalClassesAttended >= 5;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
