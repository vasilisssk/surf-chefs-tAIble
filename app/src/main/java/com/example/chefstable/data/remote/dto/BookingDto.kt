package com.example.chefstable.data.remote.dto

data class BookingDto(
    val id: String,
    val client: ClientDto?,
    val cookingClass: CookingClassDto,
    val bookingDate: String,
    val status: String,
    val rentalPackage: RentalPackageDto?,
    val cancellationDate: String?,
    val penaltyPoints: Int,
    val totalPrice: Double
)
