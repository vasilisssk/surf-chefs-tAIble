package com.example.chefstable.data.remote.dto

data class UpdateProfileRequestDto(
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val allergies: String? = null
)
