package com.example.chefstable.ui.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chefstable.data.remote.dto.BookingDto
import com.example.chefstable.data.remote.dto.RentalPackageDto
import com.example.chefstable.data.repository.BookingRepository
import com.example.chefstable.data.repository.ProfileRepository
import com.example.chefstable.data.repository.RentalPackageRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class BookingViewModel(
    private val bookingRepository: BookingRepository,
    private val profileRepository: ProfileRepository,
    private val rentalPackageRepository: RentalPackageRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _bookingSuccess = MutableLiveData<BookingDto?>(null)
    val bookingSuccess: LiveData<BookingDto?> = _bookingSuccess

    private val _allergies = MutableLiveData<String?>()
    val allergies: LiveData<String?> = _allergies

    private val _rentalPackages = MutableLiveData<List<RentalPackageDto>>(emptyList())
    val rentalPackages: LiveData<List<RentalPackageDto>> = _rentalPackages

    fun loadRentalPackages() {
        viewModelScope.launch {
            rentalPackageRepository.getRentalPackages().fold(
                onSuccess = { _rentalPackages.value = it },
                onFailure = { /* Non-critical */ }
            )
        }
    }

    fun loadProfileAllergies() {
        viewModelScope.launch {
            profileRepository.getProfile().fold(
                onSuccess = { client -> _allergies.value = client.allergies },
                onFailure = { /* Non-critical, use empty */ }
            )
        }
    }

    fun createBooking(classId: String, rentalPackageId: String, allergies: String?) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = bookingRepository.createBooking(classId, rentalPackageId, allergies)
            _isLoading.value = false

            result.fold(
                onSuccess = { booking -> _bookingSuccess.value = booking },
                onFailure = { e -> handleError(e) }
            )
        }
    }

    private fun handleError(e: Throwable) {
        _errorMessage.value = when (e) {
            is IOException -> "Нет соединения. Проверьте подключение"
            is HttpException -> when (e.code()) {
                400 -> "Нет доступных мест"
                401 -> "Необходима авторизация"
                409 -> "Бронирование конфликтует с существующим"
                in 500..599 -> "Произошла ошибка. Попробуйте позже"
                else -> "Произошла ошибка. Попробуйте позже"
            }
            else -> "Произошла ошибка. Попробуйте позже"
        }
    }

    fun onErrorMessageShown() {
        _errorMessage.value = null
    }

    fun onBookingSuccessHandled() {
        _bookingSuccess.value = null
    }
}
