package com.example.chefstable.data.repository

import com.example.chefstable.data.remote.ApiService
import com.example.chefstable.data.remote.dto.ChefDto

class ChefRepository(
    private val apiService: ApiService
) {
    suspend fun getChefs(): Result<List<ChefDto>> {
        return try {
            val response = apiService.getChefs()
            Result.success(response.chefs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChef(chefId: String): Result<ChefDto> {
        return try {
            Result.success(apiService.getChef(chefId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
