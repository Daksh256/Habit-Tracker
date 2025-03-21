package com.example.habittrackerfinal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittrackerfinal.data.repository.HabitRepository
import com.example.habittrackerfinal.domain.usecase.AddHabitUseCase

class HabitViewModelFactory(
    private val repository: HabitRepository,
    private val addHabitUseCase: AddHabitUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitViewModel(repository, addHabitUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
