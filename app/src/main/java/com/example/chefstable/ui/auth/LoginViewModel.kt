package com.example.chefstable.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chefstable.data.repository.AuthRepository
import com.example.chefstable.util.Validator
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _loginSuccess = MutableLiveData(false)
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun login(email: String, password: String) {
        val emailError = Validator.validateEmail(email)
        val passwordError = Validator.validateLoginPassword(password)

        if (emailError != null) {
            _errorMessage.value = emailError
            return
        }
        if (passwordError != null) {
            _errorMessage.value = passwordError
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _isLoading.value = false

            result.fold(
                onSuccess = { _loginSuccess.value = true },
                onFailure = { e -> handleError(e) }
            )
        }
    }

    private fun handleError(e: Throwable) {
        _errorMessage.value = when (e) {
            is IOException -> "Нет соединения. Проверьте подключение"
            is HttpException -> when (e.code()) {
                401 -> "Неверный email или пароль"
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
