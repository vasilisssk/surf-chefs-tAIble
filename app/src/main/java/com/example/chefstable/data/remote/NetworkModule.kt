package com.example.chefstable.data.remote

import com.example.chefstable.BuildConfig
import com.example.chefstable.data.SessionManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    data class NetworkStack(
        val okHttpClient: OkHttpClient,
        val retrofit: Retrofit,
        val apiService: ApiService
    )

    fun createNetworkStack(sessionManager: SessionManager): NetworkStack {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        // Lazy factory to break circular dependency (Authenticator -> Retrofit -> OkHttpClient)
        lateinit var apiService: ApiService
        val authenticator = TokenAuthenticator(sessionManager) { apiService }

        val client = OkHttpClient.Builder()
            .authenticator(authenticator)
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = createRetrofit(client)
        apiService = createApiService(retrofit)

        return NetworkStack(client, retrofit, apiService)
    }

    fun createRetrofit(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun createApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}
