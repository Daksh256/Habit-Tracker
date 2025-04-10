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

    fun handleDayClick(habitId: Int, date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            val habit = repository.getHabitById(habitId) ?: return@launch // Exit if habit not found
            val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val currentCount = repository.getCompletionCountForDate(habitId, dateString)

            if (currentCount < habit.completionsPerDay) {
                // Increment: Add a new record
                repository.addHabitRecord(
                    HabitRecordEntity(
                        habitId = habitId,
                        completionDate = dateString,
                        completionTimestamp = System.currentTimeMillis() // Timestamp might be less relevant now
                    )
                )
            } else {
                // Reset: Delete all records for this day
                repository.deleteRecordsForDate(habitId, dateString)
            }
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

    fun completeAllForToday(habitId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val habit = repository.getHabitById(habitId) ?: return@launch
            val todayString = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            val currentCount = repository.getCompletionCountForDate(habitId, todayString)
            val needed = habit.completionsPerDay - currentCount

            if (needed > 0) {
                val baseRecord = HabitRecordEntity(
                    habitId = habitId,
                    completionDate = todayString,
                    completionTimestamp = System.currentTimeMillis()
                    // id will be auto-generated
                )
                // Add the required number of records
                for (i in 1..needed) {
                    // Create a new instance or ensure ID is 0 for auto-generation if needed
                    repository.addHabitRecord(baseRecord.copy(id = 0))
                }
            }
            // Optional: If already complete, maybe reset? Or do nothing.
            // else { repository.deleteRecordsForDate(habitId, todayString) }
        }
    }

    fun getWeeklyCompletionCounts(habitId: Int): LiveData<List<Pair<LocalDate, Int>>> {
        val today = LocalDate.now()
        val sevenDaysAgo = today.minusDays(6)
        val startDateString = sevenDaysAgo.format(DateTimeFormatter.ISO_LOCAL_DATE)

        val recordsLiveData = repository.getHabitRecordsForWeekLiveData(habitId, startDateString)

        return recordsLiveData.map { records ->
            // Group records by date and count them
            val countsByDate = records
                .mapNotNull { LocalDate.parse(it.completionDate) } // Parse date string
                .filter { !it.isBefore(sevenDaysAgo) && !it.isAfter(today)} // Ensure within range
                .groupingBy { it }
                .eachCount()

            // Create list for the last 7 days, filling in counts
            (0..6).map { offset ->
                val date = today.minusDays(offset.toLong())
                //val dayName = date.dayOfWeek.name.take(3) // We need the full date for clicking
                val count = countsByDate[date] ?: 0 // Get count for the date, default to 0
                date to count // Return Pair(LocalDate, Count)
            }.reversed() // Reverse to show Mon -> Sun order
        }
    }
}
