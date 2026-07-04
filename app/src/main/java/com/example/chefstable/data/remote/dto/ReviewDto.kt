package com.example.chefstable.data.remote.dto

data class ReviewDto(
    val id: String,
    val client: ClientDto?,
    val chef: ChefDto?,
    val rating: Int,
    val comment: String?,
    val reviewDate: String
)
