package com.example.habittrackerfinal.data.repository


import com.example.habittrackerfinal.data.local.HabitDao
import com.example.habittrackerfinal.data.local.HabitEntity
import com.example.habittrackerfinal.data.local.HabitRecordEntity
import com.example.habittrackerfinal.data.model.Habit
import com.example.habittrackerfinal.domain.mapper.toHabitEntity

class HabitRepository(private val habitDao: HabitDao) {

    fun getAllHabits() = habitDao.getAll()

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
