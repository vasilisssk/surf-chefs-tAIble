package com.example.chefstable.data

import com.example.chefstable.data.remote.dto.ErrorResponseDto
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int = 0) : ApiResult<Nothing>()
    object NetworkError : ApiResult<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
    return try {
        ApiResult.Success(apiCall())
    } catch (e: IOException) {
        ApiResult.NetworkError
    } catch (e: HttpException) {
        val errorBody = parseErrorBody(e.response())
        ApiResult.Error(errorBody?.message ?: "Произошла ошибка. Попробуйте позже", e.code())
    } catch (e: Exception) {
        ApiResult.Error("Произошла ошибка. Попробуйте позже")
    }
}

private fun parseErrorBody(response: Response<*>?): ErrorResponseDto? {
    if (response == null) return null
    val errorBody = response.errorBody()?.string() ?: return null
    return try {
        Gson().fromJson(errorBody, ErrorResponseDto::class.java)
    } catch (e: JsonSyntaxException) {
        null
    }
}
