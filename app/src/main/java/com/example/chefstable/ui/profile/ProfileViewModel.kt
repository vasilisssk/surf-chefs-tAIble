package com.example.chefstable.ui.profile

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chefstable.data.remote.dto.ClientDto
import com.example.chefstable.data.repository.AuthRepository
import com.example.chefstable.data.repository.ProfileRepository
import com.example.chefstable.util.Validator
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableLiveData<ProfileState>(ProfileState.Loading)
    val state: LiveData<ProfileState> = _state

    private val _isSaving = MutableLiveData(false)
    val isSaving: LiveData<Boolean> = _isSaving

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _logoutEvent = MutableLiveData(false)
    val logoutEvent: LiveData<Boolean> = _logoutEvent

    private var currentClient: ClientDto? = null

    init {
        loadProfile()
    }

    fun loadProfile() {
        _state.value = ProfileState.Loading

        viewModelScope.launch {
            val result = profileRepository.getProfile()
            result.fold(
                onSuccess = { client ->
                    currentClient = client
                    _state.value = ProfileState.Content(client)
                },
                onFailure = { e ->
                    if (e is HttpException && e.code() == 401) {
                        _logoutEvent.value = true
                    } else {
                        _state.value = ProfileState.Error(mapError(e))
                    }
                }
            )
        }
    }

    fun saveProfile(firstName: String, phone: String, allergies: String?) {
        val nameError = Validator.validateName(firstName)
        if (nameError != null) {
            _message.value = nameError
            return
        }

        val phoneError = Validator.validatePhone(phone)
        if (phoneError != null) {
            _message.value = phoneError
            return
        }

        _isSaving.value = true

        viewModelScope.launch {
            val lastName = currentClient?.lastName
            val result = profileRepository.updateProfile(firstName, lastName, phone, allergies)
            _isSaving.value = false

            result.fold(
                onSuccess = { client ->
                    currentClient = client
                    _state.value = ProfileState.Content(client)
                    _message.value = "Профиль сохранен"
                },
                onFailure = { e -> handleError(e) }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutEvent.value = true
        }
    }

    private fun handleError(e: Throwable) {
        _message.value = mapError(e)
    }

    private fun mapError(e: Throwable): String {
        return when (e) {
            is IOException -> "Нет соединения. Проверьте подключение"
            is HttpException -> when (e.code()) {
                400 -> "Проверьте введенные данные"
                401 -> "Необходима авторизация"
                in 500..599 -> "Произошла ошибка. Попробуйте позже"
                else -> "Произошла ошибка. Попробуйте позже"
            }
            else -> "Произошла ошибка. Попробуйте позже"
        }
    }

    fun onMessageShown() {
        _message.value = null
    }

    fun onLogoutEventHandled() {
        _logoutEvent.value = false
    }
}

sealed class ProfileState {
    object Loading : ProfileState()
    data class Content(val client: ClientDto) : ProfileState()
    data class Error(val message: String) : ProfileState()
}
