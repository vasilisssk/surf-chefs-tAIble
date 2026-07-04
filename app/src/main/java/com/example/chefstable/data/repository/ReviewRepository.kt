package com.example.chefstable.data.repository

import com.example.chefstable.data.remote.ApiService
import com.example.chefstable.data.remote.dto.CreateReviewRequestDto
import com.example.chefstable.data.remote.dto.ReviewDto

class ReviewRepository(
    private val apiService: ApiService
) {
    suspend fun createReview(
        bookingId: String,
        chefId: String,
        rating: Int,
        comment: String?
    ): Result<ReviewDto> {
        return try {
            val response = apiService.createReview(
                CreateReviewRequestDto(bookingId, chefId, rating, comment)
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
