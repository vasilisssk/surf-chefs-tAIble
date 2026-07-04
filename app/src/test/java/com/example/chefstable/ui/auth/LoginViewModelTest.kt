package com.example.chefstable.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.chefstable.data.remote.dto.AuthResponseDto
import com.example.chefstable.data.remote.dto.ClientDto
import com.example.chefstable.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        fakeAuthRepository = FakeAuthRepository()
        viewModel = LoginViewModel(fakeAuthRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with invalid email shows email error`() {
        viewModel.login("not-an-email", "password123")
        assertEquals("Email имеет неверный формат", viewModel.errorMessage.value)
        assertFalse(viewModel.isLoading.value == true)
        assertFalse(viewModel.loginSuccess.value == true)
    }

    @Test
    fun `login with empty password shows password error`() {
        viewModel.login("test@example.com", "")
        assertEquals("Пароль не может быть пустым", viewModel.errorMessage.value)
        assertFalse(viewModel.isLoading.value == true)
    }

    @Test
    fun `login with valid credentials succeeds`() = runTest {
        fakeAuthRepository.loginResult = Result.success(createAuthResponse())

        viewModel.login("test@example.com", "password123")

        assertTrue(viewModel.loginSuccess.value == true)
        assertFalse(viewModel.isLoading.value == true)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `login with network error shows network message`() = runTest {
        fakeAuthRepository.loginResult = Result.failure(IOException("No connection"))

        viewModel.login("test@example.com", "password123")

        assertEquals("Нет соединения. Проверьте подключение", viewModel.errorMessage.value)
        assertFalse(viewModel.isLoading.value == true)
    }

    @Test
    fun `login with 401 shows invalid credentials message`() = runTest {
        fakeAuthRepository.loginResult = Result.failure(
            HttpException(Response.error<Any>(401, okhttp3.ResponseBody.create(null, "")))
        )

        viewModel.login("test@example.com", "wrongpass")

        assertEquals("Неверный email или пароль", viewModel.errorMessage.value)
    }

    @Test
    fun `login with 500 shows server error message`() = runTest {
        fakeAuthRepository.loginResult = Result.failure(
            HttpException(Response.error<Any>(500, okhttp3.ResponseBody.create(null, "")))
        )

        viewModel.login("test@example.com", "password123")

        assertEquals("Произошла ошибка. Попробуйте позже", viewModel.errorMessage.value)
    }

    @Test
    fun `onErrorMessageShown clears error`() {
        viewModel.login("invalid", "password123")
        viewModel.onErrorMessageShown()
        assertNull(viewModel.errorMessage.value)
    }

    private fun createAuthResponse() = AuthResponseDto(
        "token123",
        ClientDto("1", "Test", "User", "test@example.com", "+71234567890", null, 0, null, false)
    )
}

class FakeAuthRepository : AuthRepository() {

    var loginResult: Result<AuthResponseDto> = Result.success(
        AuthResponseDto("token", ClientDto("1", "Test", null, "e", "p", null, 0, null, false))
    )
    var registerResult: Result<AuthResponseDto> = Result.success(
        AuthResponseDto("token", ClientDto("1", "Test", null, "e", "p", null, 0, null, false))
    )

    override suspend fun login(email: String, password: String): Result<AuthResponseDto> {
        return loginResult
    }

    override suspend fun register(
        firstName: String,
        email: String,
        phone: String,
        password: String
    ): Result<AuthResponseDto> {
        return registerResult
    }
}
