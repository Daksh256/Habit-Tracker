package com.example.habittrackerfinal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittrackerfinal.Database.HabitRecord
import com.example.habittrackerfinal.Database.Habits
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale
import java.time.format.TextStyle

class HabitViewModel: ViewModel() {

    val habitsDao = MainApplication.habitsDatabase.habitDao()

    val habitsList: LiveData<List<Habits>> = habitsDao.getAll()

    fun addHabit(title: String, description: String?, completionsPerDay: Int, category: String, emoji: String, color: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            habitsDao.addHabits(Habits(name = title, description = description, completionsPerDay = completionsPerDay, selectedCategory = category, selectedIcon = emoji, selectedColor = color,createdDate = LocalDate.now().toString()
            ))
        }
    }

    fun deleteHabit(id: Int){
        habitsDao.deleteHabit(id)
    }

    fun completeHabit(habitId: Int) {
        viewModelScope.launch {
            val newCompletion = HabitRecord(
                habitId = habitId,
                completionDate = LocalDate.now().toString(),
                completionTimestamp = System.currentTimeMillis()
            )
            habitsDao.insertHabitRecord(newCompletion)
        }
    }

    fun getWeeklyCompletions(habitId: Int, onResult: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            // Calculate start and end of the current week
            val today = LocalDate.now()
            val startOfWeek = today.with(java.time.DayOfWeek.MONDAY).toString()  // e.g., "2025-03-16"
            val endOfWeek = today.with(java.time.DayOfWeek.SUNDAY).toString()      // e.g., "2025-03-22"
            val count = habitsDao.getHabitCompletionsForPeriod(habitId, startOfWeek, endOfWeek)
            onResult(count)
        }
    }

    fun getMonthlyCompletions(habitId: Int, onResult: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val today = LocalDate.now()
            val startOfMonth = today.withDayOfMonth(1).toString()  // e.g., "2025-03-01"
            val endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).toString()  // e.g., "2025-03-31"
            val count = habitsDao.getHabitCompletionsForPeriod(habitId, startOfMonth, endOfMonth)
            onResult(count)
        }
    }

    suspend fun getHabitStreakForWeek(habitId: Int): List<Pair<String, Boolean>> {
        val streak = mutableListOf<Pair<String, Boolean>>()
        val today = LocalDate.now()

        for (i in 0..6) {
            val date = today.minusDays(i.toLong())
            val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            val isCompleted = habitsDao.isHabitCompletedOnDate(habitId, date.toString())
            streak.add(Pair(dayOfWeek, isCompleted))
        }

        return streak.reversed()
    }

}