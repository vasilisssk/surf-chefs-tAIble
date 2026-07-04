package com.example.chefstable.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chefstable.data.remote.dto.ChefDto
import com.example.chefstable.data.remote.dto.CookingClassDto
import com.example.chefstable.data.repository.ChefRepository
import com.example.chefstable.data.repository.ClassRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ScheduleViewModel(
    private val classRepository: ClassRepository,
    private val chefRepository: ChefRepository
) : ViewModel() {

    private val _state = MutableLiveData<ScheduleState>(ScheduleState.Loading)
    val state: LiveData<ScheduleState> = _state

    private val _chefs = MutableLiveData<List<ChefDto>>(emptyList())
    val chefs: LiveData<List<ChefDto>> = _chefs

    private var selectedChefId: String? = null

    init {
        loadChefs()
        loadClasses()
    }

    fun loadClasses() {
        _state.value = ScheduleState.Loading

        viewModelScope.launch {
            val result = classRepository.getClasses(chefId = selectedChefId)
            result.fold(
                onSuccess = { classes ->
                    if (classes.isEmpty()) {
                        _state.value = ScheduleState.Empty
                    } else {
                        _state.value = ScheduleState.Content(classes)
                    }
                },
                onFailure = { e ->
                    _state.value = ScheduleState.Error(mapError(e))
                }
            )
        }
    }

    private fun loadChefs() {
        viewModelScope.launch {
            chefRepository.getChefs().fold(
                onSuccess = { _chefs.value = it },
                onFailure = { /* Non-critical, ignore */ }
            )
        }
    }

    fun filterByChef(chefId: String?) {
        selectedChefId = chefId
        loadClasses()
    }

    fun resetFilters() {
        selectedChefId = null
        loadClasses()
    }

    private fun mapError(e: Throwable): String {
        return when (e) {
            is IOException -> "Нет соединения. Проверьте подключение"
            is HttpException -> "Произошла ошибка. Попробуйте позже"
            else -> "Произошла ошибка. Попробуйте позже"
        }
    }
}

sealed class ScheduleState {
    object Loading : ScheduleState()
    data class Content(val classes: List<CookingClassDto>) : ScheduleState()
    object Empty : ScheduleState()
    data class Error(val message: String) : ScheduleState()
}
