package com.example.chefstable.data.remote.dto

data class CreateBookingRequestDto(
    val classId: String,
    val rentalPackageId: String,
    val allergies: String?
)
