package com.example.chefstable.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chefstable.data.repository.BookingRepository
import com.example.chefstable.data.repository.ClassRepository
import com.example.chefstable.data.remote.dto.BookingDto
import com.example.chefstable.data.remote.dto.CookingClassDto
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeViewModel(
    private val classRepository: ClassRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _state = MutableLiveData<HomeState>(HomeState.Loading)
    val state: LiveData<HomeState> = _state

    fun loadData() {
        _state.value = HomeState.Loading

        viewModelScope.launch {
            var bookings: List<BookingDto> = emptyList()
            var classes: List<CookingClassDto> = emptyList()
            var error: String? = null

            val bookingsResult = bookingRepository.getBookings("upcoming")
            bookingsResult.fold(
                onSuccess = { bookings = it },
                onFailure = { e ->
                    if (error == null) error = mapError(e)
                }
            )

            val classesResult = classRepository.getClasses()
            classesResult.fold(
                onSuccess = { classes = it },
                onFailure = { e ->
                    if (error == null) error = mapError(e)
                }
            )

            if (error != null) {
                _state.value = HomeState.Error(error!!)
            } else if (bookings.isEmpty() && classes.isEmpty()) {
                _state.value = HomeState.Empty
            } else {
                _state.value = HomeState.Content(bookings, classes)
            }
        }
    }

    private fun mapError(e: Throwable): String {
        return when (e) {
            is IOException -> "Нет соединения. Проверьте подключение"
            is HttpException -> "Произошла ошибка. Попробуйте позже"
            else -> "Произошла ошибка. Попробуйте позже"
        }
    }
}

sealed class HomeState {
    object Loading : HomeState()
    data class Content(
        val bookings: List<BookingDto>,
        val classes: List<CookingClassDto>
    ) : HomeState()
    object Empty : HomeState()
    data class Error(val message: String) : HomeState()
}