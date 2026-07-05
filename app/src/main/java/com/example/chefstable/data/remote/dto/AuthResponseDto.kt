package com.example.chefstable.data.remote.dto

data class AuthResponseDto(
    val token: String,
    val refreshToken: String,
    val client: ClientDto
)
