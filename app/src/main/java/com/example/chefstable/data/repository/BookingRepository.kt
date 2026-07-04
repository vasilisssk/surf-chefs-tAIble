package com.example.chefstable.data.repository

import com.example.chefstable.data.remote.ApiService
import com.example.chefstable.data.remote.dto.BookingDto
import com.example.chefstable.data.remote.dto.CreateBookingRequestDto
import com.example.chefstable.data.remote.dto.UpdateBookingRequestDto

open class BookingRepository(
    private val apiService: ApiService? = null
) {
    open suspend fun getBookings(status: String? = null): Result<List<BookingDto>> {
        return try {
            val response = apiService!!.getBookings(status)
            Result.success(response.bookings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun getBooking(bookingId: String): Result<BookingDto> {
        return try {
            Result.success(apiService!!.getBooking(bookingId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun createBooking(
        classId: String,
        rentalPackageId: String,
        allergies: String?
    ): Result<BookingDto> {
        return try {
            val response = apiService!!.createBooking(
                CreateBookingRequestDto(classId, rentalPackageId, allergies)
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun cancelBooking(bookingId: String, reason: String? = null): Result<BookingDto> {
        return try {
            val response = apiService!!.updateBooking(
                bookingId,
                UpdateBookingRequestDto("cancel", reason)
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
