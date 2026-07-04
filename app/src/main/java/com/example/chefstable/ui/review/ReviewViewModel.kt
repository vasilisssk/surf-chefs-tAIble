package com.example.chefstable.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chefstable.data.repository.ReviewRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ReviewViewModel(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _success = MutableLiveData(false)
    val success: LiveData<Boolean> = _success

    fun submitReview(bookingId: String, chefId: String, rating: Int, comment: String?) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = reviewRepository.createReview(bookingId, chefId, rating, comment)
            _isLoading.value = false

            result.fold(
                onSuccess = { _success.value = true },
                onFailure = { e -> handleError(e) }
            )
        }
    }

    private fun handleError(e: Throwable) {
        _errorMessage.value = when (e) {
            is IOException -> "Нет соединения. Проверьте подключение"
            is HttpException -> when (e.code()) {
                400 -> "Некорректные данные отзыва"
                409 -> "Отзыв уже оставлен для этого бронирования"
                in 500..599 -> "Произошла ошибка. Попробуйте позже"
                else -> "Произошла ошибка. Попробуйте позже"
            }
            else -> "Произошла ошибка. Попробуйте позже"
        }
    }

    fun onErrorMessageShown() {
        _errorMessage.value = null
    }
}
