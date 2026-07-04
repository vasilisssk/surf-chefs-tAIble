package com.example.chefstable.data.remote.dto

data class CreateReviewRequestDto(
    val bookingId: String,
    val chefId: String,
    val rating: Int,
    val comment: String?
)
