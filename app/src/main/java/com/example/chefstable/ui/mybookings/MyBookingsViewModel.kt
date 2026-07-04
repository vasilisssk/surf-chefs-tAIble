package com.example.chefstable.ui.mybookings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chefstable.data.remote.dto.BookingDto
import com.example.chefstable.data.repository.BookingRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MyBookingsViewModel(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _state = MutableLiveData<MyBookingsState>(MyBookingsState.Loading)
    val state: LiveData<MyBookingsState> = _state

    private val _cancelMessage = MutableLiveData<String?>()
    val cancelMessage: LiveData<String?> = _cancelMessage

    private var currentFilter: String? = null

    init {
        loadBookings()
    }

    fun loadBookings() {
        _state.value = MyBookingsState.Loading

        viewModelScope.launch {
            val result = bookingRepository.getBookings(currentFilter)
            result.fold(
                onSuccess = { bookings ->
                    if (bookings.isEmpty()) {
                        _state.value = MyBookingsState.Empty
                    } else {
                        _state.value = MyBookingsState.Content(bookings)
                    }
                },
                onFailure = { e ->
                    _state.value = MyBookingsState.Error(mapError(e))
                }
            )
        }
    }

    fun filterBy(status: String?) {
        currentFilter = status
        loadBookings()
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            val result = bookingRepository.cancelBooking(bookingId)
            result.fold(
                onSuccess = { loadBookings() },
                onFailure = { e ->
                    _cancelMessage.value = when (e) {
                        is HttpException -> when (e.code()) {
                            400 -> "Невозможно отменить бронирование менее чем за 24 часа до начала"
                            404 -> "Бронирование не найдено"
                            in 500..599 -> "Произошла ошибка. Попробуйте позже"
                            else -> "Произошла ошибка. Попробуйте позже"
                        }
                        is IOException -> "Нет соединения. Проверьте подключение"
                        else -> "Произошла ошибка. Попробуйте позже"
                    }
                }
            )
        }
    }

    fun onCancelMessageShown() {
        _cancelMessage.value = null
    }

    private fun mapError(e: Throwable): String {
        return when (e) {
            is IOException -> "Нет соединения. Проверьте подключение"
            is HttpException -> when (e.code()) {
                401 -> "Необходима авторизация"
                else -> "Произошла ошибка. Попробуйте позже"
            }
            else -> "Произошла ошибка. Попробуйте позже"
        }
    }
}

sealed class MyBookingsState {
    object Loading : MyBookingsState()
    data class Content(val bookings: List<BookingDto>) : MyBookingsState()
    object Empty : MyBookingsState()
    data class Error(val message: String) : MyBookingsState()
}
