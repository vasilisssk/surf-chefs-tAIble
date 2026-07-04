package com.chefstable.backend.profile.dto;

import java.time.OffsetDateTime;

public record ClientResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        String phone,
        OffsetDateTime registrationDate,
        int totalClassesAttended,
        String allergies,
        boolean isPermanentClient
) {
}
