package com.example.chefstable.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ClientDto(
    val id: String,
    val firstName: String,
    val lastName: String?,
    val email: String,
    val phone: String,
    val registrationDate: String?,
    val totalClassesAttended: Int,
    val allergies: String?,
    val isPermanentClient: Boolean
)
