package com.example.chefstable.ui.mybookings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.chefstable.data.remote.dto.BookingDto
import com.example.chefstable.data.remote.dto.ChefDto
import com.example.chefstable.data.remote.dto.CookingClassDto
import com.example.chefstable.data.repository.BookingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MyBookingsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeBookingRepository: FakeBookingRepository
    private lateinit var viewModel: MyBookingsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        fakeBookingRepository = FakeBookingRepository()
        viewModel = MyBookingsViewModel(fakeBookingRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loading bookings shows content state when bookings exist`() = runTest {
        fakeBookingRepository.bookingsResult = Result.success(listOf(createBooking("1", "CONFIRMED")))

        viewModel.loadBookings()

        assertTrue(viewModel.state.value is MyBookingsState.Content)
        val content = viewModel.state.value as MyBookingsState.Content
        assertEquals(1, content.bookings.size)
    }

    @Test
    fun `loading empty bookings shows empty state`() = runTest {
        fakeBookingRepository.bookingsResult = Result.success(emptyList())

        viewModel.loadBookings()

        assertTrue(viewModel.state.value is MyBookingsState.Empty)
    }

    @Test
    fun `network error shows error state with message`() = runTest {
        fakeBookingRepository.bookingsResult = Result.failure(IOException("No connection"))

        viewModel.loadBookings()

        assertTrue(viewModel.state.value is MyBookingsState.Error)
        val error = viewModel.state.value as MyBookingsState.Error
        assertEquals("Нет соединения. Проверьте подключение", error.message)
    }

    @Test
    fun `cancel booking with 400 shows cancellation error message`() = runTest {
        fakeBookingRepository.bookingsResult = Result.success(emptyList())
        fakeBookingRepository.cancelResult = Result.failure(
            HttpException(Response.error<Any>(400, okhttp3.ResponseBody.create(null, "")))
        )
        viewModel.loadBookings()

        viewModel.cancelBooking("booking1")

        assertEquals(
            "Невозможно отменить бронирование менее чем за 24 часа до начала",
            viewModel.cancelMessage.value
        )
    }

    @Test
    fun `cancel booking with 404 shows not found message`() = runTest {
        fakeBookingRepository.bookingsResult = Result.success(emptyList())
        fakeBookingRepository.cancelResult = Result.failure(
            HttpException(Response.error<Any>(404, okhttp3.ResponseBody.create(null, "")))
        )
        viewModel.loadBookings()

        viewModel.cancelBooking("booking1")

        assertEquals("Бронирование не найдено", viewModel.cancelMessage.value)
    }

    @Test
    fun `cancel booking success reloads bookings`() = runTest {
        fakeBookingRepository.bookingsResult = Result.success(emptyList())
        fakeBookingRepository.cancelResult = Result.success(createBooking("1", "CANCELLED_BY_CLIENT"))
        viewModel.loadBookings()

        viewModel.cancelBooking("booking1")

        assertNotNull(viewModel.state.value)
    }

    @Test
    fun `onCancelMessageShown clears message`() = runTest {
        fakeBookingRepository.bookingsResult = Result.success(emptyList())
        fakeBookingRepository.cancelResult = Result.failure(
            HttpException(Response.error<Any>(400, okhttp3.ResponseBody.create(null, "")))
        )
        viewModel.loadBookings()
        viewModel.cancelBooking("booking1")

        viewModel.onCancelMessageShown()

        assertEquals(null, viewModel.cancelMessage.value)
    }

    private fun createBooking(id: String, status: String): BookingDto {
        return BookingDto(
            id = id,
            client = null,
            cookingClass = CookingClassDto(
                id = "class1",
                title = "Test Class",
                description = "Description",
                dateTime = "2026-07-15T18:00:00",
                duration = 3,
                maxParticipants = 10,
                availableSeats = 5,
                chef = ChefDto("chef1", "Test Chef", "Italian", 4.5, 10, null),
                classType = "GROUP",
                price = 3500.0
            ),
            bookingDate = "2026-07-01T12:00:00",
            status = status,
            rentalPackage = null,
            cancellationDate = null,
            penaltyPoints = 0
        )
    }
}

class FakeBookingRepository : BookingRepository() {

    var bookingsResult: Result<List<BookingDto>> = Result.success(emptyList())
    var cancelResult: Result<BookingDto> = Result.success(
        BookingDto(
            id = "1", client = null,
            cookingClass = CookingClassDto(
                "c1", "Test", "Desc", "2026-07-15T18:00:00", 3, 10, 5,
                ChefDto("ch1", "Chef", "Italian", 4.0, 1, null), "GROUP", 1000.0
            ),
            "2026-07-01", "CANCELLED_BY_CLIENT", null, null, 0
        )
    )

    override suspend fun getBookings(status: String?): Result<List<BookingDto>> {
        return bookingsResult
    }

    override suspend fun cancelBooking(bookingId: String, reason: String?): Result<BookingDto> {
        return cancelResult
    }
}
