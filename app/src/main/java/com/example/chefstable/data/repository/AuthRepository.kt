package com.example.chefstable.data.repository

import com.example.chefstable.data.SessionManager
import com.example.chefstable.data.remote.ApiService
import com.example.chefstable.data.remote.dto.AuthResponseDto
import com.example.chefstable.data.remote.dto.LoginRequestDto
import com.example.chefstable.data.remote.dto.RegistrationRequestDto

open class AuthRepository(
    private val apiService: ApiService? = null,
    private val sessionManager: SessionManager? = null
) {
    open suspend fun login(email: String, password: String): Result<AuthResponseDto> {
        return try {
            val response = apiService!!.login(LoginRequestDto(email, password))
            sessionManager!!.saveToken(response.token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun register(
        firstName: String,
        email: String,
        phone: String,
        password: String
    ): Result<AuthResponseDto> {
        return try {
            val response = apiService!!.register(
                RegistrationRequestDto(firstName, email, phone, password)
            )
            sessionManager!!.saveToken(response.token)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun logout(): Result<Unit> {
        return try {
            apiService!!.logout()
            sessionManager!!.clearToken()
            Result.success(Unit)
        } catch (e: Exception) {
            sessionManager!!.clearToken()
            Result.success(Unit)
        }
    }

    open fun isLoggedIn(): Boolean = sessionManager?.isLoggedIn() ?: false
}
