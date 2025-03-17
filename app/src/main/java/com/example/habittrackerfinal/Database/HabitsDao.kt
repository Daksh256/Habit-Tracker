package com.example.habittrackerfinal.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HabitDao {

    @Query("SELECT * FROM HABITS")
    fun getAll(): LiveData<List<Habits>>

    @Insert
    fun addHabits(habit: Habits)

    @Query("DELETE FROM HABITS WHERE id = :id")
    fun deleteHabit(id: Int)

    @Insert
    suspend fun insertHabitRecord(habitRecord: HabitRecord)

    @Query("""
    SELECT COUNT(*) > 0 FROM habit_records
    WHERE habitId = :habitId AND completionDate = :date
""")
    suspend fun isHabitCompletedOnDate(habitId: Int, date: String): Boolean


    @Query("""
    SELECT COUNT(*) FROM habit_records 
    WHERE habitId = :habitId 
    AND completionDate BETWEEN :startDate AND :endDate
""")
    suspend fun getHabitCompletionsForPeriod(habitId: Int, startDate: String, endDate: String): Int

}