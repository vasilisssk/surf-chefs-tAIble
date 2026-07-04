package com.chefstable.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank String firstName,
        @Email @NotBlank String email,
        @NotBlank String phone,
        @Size(min = 8) String password
) {
}
