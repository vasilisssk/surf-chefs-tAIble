package com.example.chefstable.data.remote

import com.example.chefstable.data.SessionManager
import com.example.chefstable.data.remote.dto.RefreshTokenRequestDto
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.util.concurrent.locks.ReentrantLock

class TokenAuthenticator(
    private val sessionManager: SessionManager,
    private val apiServiceFactory: () -> ApiService
) : Authenticator {

    private val lock = ReentrantLock()

    override fun authenticate(route: Route?, response: Response): Request? {
        // Avoid infinite loops: if this is already a retry for /auth/refresh, give up
        if (response.request.url.encodedPath.contains("/auth/refresh")) {
            sessionManager.clearToken()
            return null
        }

        lock.lock()
        try {
            val refreshToken = sessionManager.getRefreshToken() ?: run {
                sessionManager.clearToken()
                return null
            }

            // Check if token was already refreshed by another thread
            val currentToken = sessionManager.getToken()
            val requestToken = response.request.header("Authorization")?.substringAfter("Bearer ")
            if (currentToken != null && currentToken != requestToken) {
                // Token was already refreshed, retry with new token
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            // Try to refresh the token
            try {
                val apiService = apiServiceFactory()
                val refreshResponse = runBlocking { apiService.refresh(RefreshTokenRequestDto(refreshToken)) }
                sessionManager.saveToken(refreshResponse.token)
                sessionManager.saveRefreshToken(refreshResponse.refreshToken)

                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${refreshResponse.token}")
                    .build()
            } catch (e: Exception) {
                sessionManager.clearToken()
                return null
            }
        } finally {
            lock.unlock()
        }
    }
}
