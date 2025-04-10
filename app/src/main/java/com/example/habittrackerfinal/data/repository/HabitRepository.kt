package com.example.habittrackerfinal.data.repository


import androidx.lifecycle.LiveData
import com.example.habittrackerfinal.data.local.HabitDao
import com.example.habittrackerfinal.data.local.HabitEntity
import com.example.habittrackerfinal.data.local.HabitRecordEntity
import com.example.habittrackerfinal.data.model.Habit
import com.example.habittrackerfinal.domain.mapper.toHabit
import com.example.habittrackerfinal.domain.mapper.toHabitEntity

class HabitRepository(private val habitDao: HabitDao) {

    // New: Get single habit
    suspend fun getHabitById(id: Int): Habit? {
        return habitDao.getHabitById(id)?.toHabit()
    }

    suspend fun getCompletionCountForDate(habitId: Int, date: String): Int {
        return habitDao.getCompletionCountForDate(habitId, date)
    }

    // Optional LiveData version
    fun getCompletionCountForDateLiveData(habitId: Int, date: String): LiveData<Int> {
        return habitDao.getCompletionCountForDateLiveData(habitId, date)
    }

    suspend fun deleteRecordsForDate(habitId: Int, date: String) {
        habitDao.deleteRecordsForDate(habitId, date)
    }

    //fun getAllHabits() = habitDao.getAll()
    fun getAllHabits(): LiveData<List<HabitEntity>> = habitDao.getAllHabits()

    fun getHabitRecordsForWeekLiveData(habitId: Int, startDate: String): LiveData<List<HabitRecordEntity>> {
        return habitDao.getHabitRecordsForWeekLiveData(habitId, startDate)
    }

    suspend fun addHabit(habit: Habit) {
        val entity = habit.toHabitEntity() // Convert to DB entity
        habitDao.insertHabit(entity)
    }

    suspend fun deleteHabit(id: Int) {
        habitDao.deleteHabit(id)
    }

    suspend fun addHabitRecord(record: HabitRecordEntity) {
        habitDao.insertHabitRecord(record)
    }

    suspend fun getHabitCompletionsForPeriod(
        habitId: Int,
        startDate: String,
        endDate: String
    ): Int {
        return habitDao.getHabitCompletionsForPeriod(habitId, startDate, endDate)
    }

    suspend fun isHabitCompletedOnDate(habitId: Int, date: String): Boolean {
        return habitDao.isHabitCompletedOnDate(habitId, date)
    }
}
