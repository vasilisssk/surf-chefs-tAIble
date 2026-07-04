package com.example.chefstable.data.remote.dto

data class ChefDto(
    val id: String,
    val name: String,
    val specialization: String,
    val rating: Double,
    val totalReviews: Int,
    val bio: String?
)
