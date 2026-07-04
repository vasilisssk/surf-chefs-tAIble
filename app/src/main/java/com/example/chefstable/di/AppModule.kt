package com.example.chefstable.di

import com.example.chefstable.data.SessionManager
import com.example.chefstable.data.remote.ApiService
import com.example.chefstable.data.remote.NetworkModule
import com.example.chefstable.data.repository.AuthRepository
import com.example.chefstable.data.repository.BookingRepository
import com.example.chefstable.data.repository.ChefRepository
import com.example.chefstable.data.repository.ClassRepository
import com.example.chefstable.data.repository.ProfileRepository
import com.example.chefstable.data.repository.RentalPackageRepository
import com.example.chefstable.data.repository.ReviewRepository
import com.example.chefstable.ui.auth.LoginViewModel
import com.example.chefstable.ui.auth.RegistrationViewModel
import com.example.chefstable.ui.booking.BookingViewModel
import com.example.chefstable.ui.classdetail.ClassDetailViewModel
import com.example.chefstable.ui.home.HomeViewModel
import com.example.chefstable.ui.mybookings.MyBookingsViewModel
import com.example.chefstable.ui.notifications.NotificationsViewModel
import com.example.chefstable.ui.profile.ProfileViewModel
import com.example.chefstable.ui.review.ReviewViewModel
import com.example.chefstable.ui.schedule.ScheduleViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {

    single { SessionManager(androidContext()) }

    single { NetworkModule.createOkHttpClient(get()) }

    single { NetworkModule.createRetrofit(get<OkHttpClient>()) }

    single { NetworkModule.createApiService(get<Retrofit>()) }

    single { AuthRepository(get(), get()) }
    single { ProfileRepository(get()) }
    single { ClassRepository(get()) }
    single { BookingRepository(get()) }
    single { ChefRepository(get()) }
    single { ReviewRepository(get()) }
    single { RentalPackageRepository(get()) }

    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { ScheduleViewModel(get(), get()) }
    viewModel { ClassDetailViewModel(get(), get(), get()) }
    viewModel { BookingViewModel(get(), get(), get()) }
    viewModel { MyBookingsViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { NotificationsViewModel() }
    viewModel { ReviewViewModel(get()) }
}
