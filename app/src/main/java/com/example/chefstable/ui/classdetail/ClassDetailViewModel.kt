package com.example.chefstable.ui.classdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chefstable.data.remote.dto.CookingClassDto
import com.example.chefstable.data.remote.dto.RentalPackageDto
import com.example.chefstable.data.repository.ClassRepository
import com.example.chefstable.data.repository.RentalPackageRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ClassDetailViewModel(
    private val classRepository: ClassRepository,
    private val rentalPackageRepository: RentalPackageRepository,
    private val reviewRepository: com.example.chefstable.data.repository.ReviewRepository
) : ViewModel() {

    private val _state = MutableLiveData<ClassDetailState>(ClassDetailState.Loading)
    val state: LiveData<ClassDetailState> = _state

    private val _rentalPackages = MutableLiveData<List<RentalPackageDto>>(emptyList())
    val rentalPackages: LiveData<List<RentalPackageDto>> = _rentalPackages

    fun loadClass(classId: String) {
        _state.value = ClassDetailState.Loading

        viewModelScope.launch {
            val result = classRepository.getClass(classId)
            result.fold(
                onSuccess = { cls ->
                    _state.value = ClassDetailState.Content(cls)
                    loadRentalPackages()
                },
                onFailure = { e ->
                    if (e is HttpException && e.code() == 404) {
                        _state.value = ClassDetailState.NotFound
                    } else {
                        _state.value = ClassDetailState.Error(mapError(e))
                    }
                }
            )
        }
    }

    private fun loadRentalPackages() {
        viewModelScope.launch {
            rentalPackageRepository.getRentalPackages().fold(
                onSuccess = { _rentalPackages.value = it },
                onFailure = { /* Non-critical */ }
            )
        }
    }

    private fun mapError(e: Throwable): String {
        return when (e) {
            is IOException -> "Нет соединения. Проверьте подключение"
            is HttpException -> "Произошла ошибка. Попробуйте позже"
            else -> "Произошла ошибка. Попробуйте позже"
        }
    }
}

sealed class ClassDetailState {
    object Loading : ClassDetailState()
    data class Content(val classData: CookingClassDto) : ClassDetailState()
    object NotFound : ClassDetailState()
    data class Error(val message: String) : ClassDetailState()
}
