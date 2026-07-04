package com.example.chefstable.data.repository

import com.example.chefstable.data.remote.ApiService
import com.example.chefstable.data.remote.dto.ClientDto
import com.example.chefstable.data.remote.dto.UpdateProfileRequestDto

class ProfileRepository(
    private val apiService: ApiService
) {
    suspend fun getProfile(): Result<ClientDto> {
        return try {
            Result.success(apiService.getProfile())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(
        firstName: String?,
        lastName: String?,
        phone: String?,
        allergies: String?
    ): Result<ClientDto> {
        return try {
            val response = apiService.updateProfile(
                UpdateProfileRequestDto(firstName, lastName, phone, allergies)
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
