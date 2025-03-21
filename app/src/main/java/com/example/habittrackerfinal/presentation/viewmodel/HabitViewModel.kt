package com.example.habittrackerfinal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.habittrackerfinal.data.local.HabitRecordEntity
import com.example.habittrackerfinal.data.model.Habit
import com.example.habittrackerfinal.data.repository.HabitRepository
import com.example.habittrackerfinal.domain.mapper.toHabit
import com.example.habittrackerfinal.domain.usecase.AddHabitUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class HabitViewModel(
    private val repository: HabitRepository,
    private val addHabitUseCase: AddHabitUseCase
) : ViewModel() {

    // ✅ Convert HabitEntity to Habit inside Flow
    private val _habitsList = repository.getAllHabits()
        .map { list -> list.map { it.toHabit() } } // Convert HabitEntity -> Habit
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val habitsList: StateFlow<List<Habit>> = _habitsList

    // ✅ Use AddHabitUseCase instead of directly using repository
    fun addHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            addHabitUseCase(habit)
        }
    }

    fun deleteHabit(id: Int) {
        viewModelScope.launch {
            repository.deleteHabit(id)
        }
    }

    fun completeHabit(habitId: Int) {
        viewModelScope.launch {
            repository.addHabitRecord(
                HabitRecordEntity(
                    habitId = habitId,
                    completionDate = LocalDate.now().toString(),
                    completionTimestamp = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun getHabitStreakForWeek(habitId: Int): List<Pair<String, Boolean>> {
        val today = LocalDate.now()
        return (0..6).map { offset ->
            val date = today.minusDays(offset.toLong())
            val dayName = date.dayOfWeek.name.take(3) // "MON", "TUE", etc.
            val completed = repository.isHabitCompletedOnDate(habitId, date.toString())
            dayName to completed
        }.reversed()
    }
}
