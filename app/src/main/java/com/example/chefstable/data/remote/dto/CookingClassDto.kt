package com.example.chefstable.data.remote.dto

data class CookingClassDto(
    val id: String,
    val title: String,
    val description: String,
    val dateTime: String,
    val duration: Int,
    val maxParticipants: Int,
    val availableSeats: Int,
    val chef: ChefDto,
    val classType: String,
    val price: Double
)
