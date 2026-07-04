package com.chefstable.backend.auth.dto;

import com.chefstable.backend.profile.dto.ClientResponse;

public record AuthResponse(String token, ClientResponse client) {
}
