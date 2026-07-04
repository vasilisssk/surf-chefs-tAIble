package com.example.chefstable.data.repository

import com.example.chefstable.data.remote.ApiService
import com.example.chefstable.data.remote.dto.CookingClassDto

class ClassRepository(
    private val apiService: ApiService
) {
    suspend fun getClasses(
        dateFrom: String? = null,
        dateTo: String? = null,
        chefId: String? = null,
        classType: String? = null
    ): Result<List<CookingClassDto>> {
        return try {
            val response = apiService.getClasses(dateFrom, dateTo, chefId, classType)
            Result.success(response.classes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getClass(classId: String): Result<CookingClassDto> {
        return try {
            Result.success(apiService.getClass(classId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
