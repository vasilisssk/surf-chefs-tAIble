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

class RegistrationViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _fieldErrors = MutableLiveData<Map<String, String?>>(emptyMap())
    val fieldErrors: LiveData<Map<String, String?>> = _fieldErrors

    private val _registrationSuccess = MutableLiveData(false)
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    fun register(firstName: String, email: String, phone: String, password: String) {
        val errors = mutableMapOf<String, String?>()

        errors["firstName"] = Validator.validateName(firstName)
        errors["email"] = Validator.validateEmail(email)
        errors["phone"] = Validator.validatePhone(phone)
        errors["password"] = Validator.validatePassword(password)

        _fieldErrors.value = errors

        if (errors.values.any { it != null }) {
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            val result = authRepository.register(firstName, email, phone, password)
            _isLoading.value = false

            result.fold(
                onSuccess = { _registrationSuccess.value = true },
                onFailure = { e -> handleError(e) }
            )
        }
    }

    private fun handleError(e: Throwable) {
        _errorMessage.value = when (e) {
            is IOException -> "Нет соединения. Проверьте подключение"
            is HttpException -> when (e.code()) {
                400 -> "Проверьте введенные данные"
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
