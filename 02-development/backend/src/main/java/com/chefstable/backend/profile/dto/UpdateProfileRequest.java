package com.chefstable.backend.profile.dto;

public record UpdateProfileRequest(
        String firstName,
        String lastName,
        String phone,
        String allergies
) {
}
