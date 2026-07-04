package com.example.chefstable.data.remote

import com.example.chefstable.data.remote.dto.AuthResponseDto
import com.example.chefstable.data.remote.dto.BookingDto
import com.example.chefstable.data.remote.dto.BookingListDto
import com.example.chefstable.data.remote.dto.ChefDto
import com.example.chefstable.data.remote.dto.ChefListDto
import com.example.chefstable.data.remote.dto.ClientDto
import com.example.chefstable.data.remote.dto.CookingClassDto
import com.example.chefstable.data.remote.dto.CookingClassListDto
import com.example.chefstable.data.remote.dto.CreateBookingRequestDto
import com.example.chefstable.data.remote.dto.CreateReviewRequestDto
import com.example.chefstable.data.remote.dto.LoginRequestDto
import com.example.chefstable.data.remote.dto.RegistrationRequestDto
import com.example.chefstable.data.remote.dto.RentalPackageListDto
import com.example.chefstable.data.remote.dto.ReviewDto
import com.example.chefstable.data.remote.dto.UpdateBookingRequestDto
import com.example.chefstable.data.remote.dto.UpdateProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body request: RegistrationRequestDto): AuthResponseDto

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("auth/logout")
    suspend fun logout(): retrofit2.Response<Unit>

    @GET("profile")
    suspend fun getProfile(): ClientDto

    @PUT("profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequestDto): ClientDto

    @GET("classes")
    suspend fun getClasses(
        @Query("date_from") dateFrom: String? = null,
        @Query("date_to") dateTo: String? = null,
        @Query("chef_id") chefId: String? = null,
        @Query("class_type") classType: String? = null
    ): CookingClassListDto

    @GET("classes/{classId}")
    suspend fun getClass(@Path("classId") classId: String): CookingClassDto

    @GET("bookings")
    suspend fun getBookings(@Query("status") status: String? = null): BookingListDto

    @POST("bookings")
    suspend fun createBooking(@Body request: CreateBookingRequestDto): BookingDto

    @GET("bookings/{bookingId}")
    suspend fun getBooking(@Path("bookingId") bookingId: String): BookingDto

    @PUT("bookings/{bookingId}")
    suspend fun updateBooking(
        @Path("bookingId") bookingId: String,
        @Body request: UpdateBookingRequestDto
    ): BookingDto

    @POST("reviews")
    suspend fun createReview(@Body request: CreateReviewRequestDto): ReviewDto

    @GET("chefs")
    suspend fun getChefs(): ChefListDto

    @GET("chefs/{chefId}")
    suspend fun getChef(@Path("chefId") chefId: String): ChefDto

    @GET("rental-packages")
    suspend fun getRentalPackages(): RentalPackageListDto
}
