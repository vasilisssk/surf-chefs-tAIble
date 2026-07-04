package com.example.chefstable.data.repository

import com.example.chefstable.data.remote.ApiService
import com.example.chefstable.data.remote.dto.RentalPackageDto

class RentalPackageRepository(
    private val apiService: ApiService
) {
    suspend fun getRentalPackages(): Result<List<RentalPackageDto>> {
        return try {
            val response = apiService.getRentalPackages()
            Result.success(response.packages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
