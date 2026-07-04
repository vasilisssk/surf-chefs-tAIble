package com.example.chefstable.data.remote.dto

data class RentalPackageDto(
    val id: String,
    val packageName: String,
    val description: String,
    val price: Double,
    val availableCount: Int
)
