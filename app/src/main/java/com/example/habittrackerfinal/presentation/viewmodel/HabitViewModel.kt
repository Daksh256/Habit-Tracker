package com.example.habittrackerfinal.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.habittrackerfinal.data.local.HabitRecordEntity
import com.example.habittrackerfinal.data.model.Habit
import com.example.habittrackerfinal.data.repository.HabitRepository
import com.example.habittrackerfinal.domain.mapper.toHabit
import com.example.habittrackerfinal.domain.usecase.AddHabitUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HabitViewModel(
    private val repository: HabitRepository,
    private val addHabitUseCase: AddHabitUseCase
) : ViewModel() {

    // Convert LiveData<HabitEntity> -> LiveData<Habit>
    val habitsList: LiveData<List<Habit>> = repository.getAllHabits().map { list ->
        list.map { it.toHabit() }
    }

    fun addHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            addHabitUseCase(habit)
        }
    }

    fun deleteHabit(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHabit(id)
        }
    }

    fun completeHabit(habitId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun getWeeklyStreakLiveData(habitId: Int): LiveData<List<Pair<String, Boolean>>> {
        val today = LocalDate.now()
        val sevenDaysAgo = today.minusDays(6) // Start date for the last 7 days
        val startDateString = sevenDaysAgo.format(DateTimeFormatter.ISO_LOCAL_DATE) // Format needed for query

        // Get the LiveData of records from the repository
        val recordsLiveData = repository.getHabitRecordsForWeekLiveData(habitId, startDateString)

        // Transform the LiveData<List<HabitRecordEntity>> to LiveData<List<Pair<String, Boolean>>>
        return recordsLiveData.map { records ->
            val recordsMap = records.associateBy { LocalDate.parse(it.completionDate) } // Map date string to record for quick lookup
            (0..6).map { offset ->
                val date = today.minusDays(offset.toLong())
                val dayName = date.dayOfWeek.name.take(3) // "MON", "TUE", etc.
                // Check if a record exists for this specific date in the fetched records
                val completed = recordsMap.containsKey(date)
                dayName to completed
            }.reversed() // Reverse to show Mon -> Sun or equivalent order
        }
    }
}
