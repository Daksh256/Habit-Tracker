package com.example.habittrackerfinal.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface HabitDao {

    @Query("SELECT * FROM habits")
    fun getAllHabits(): LiveData<List<HabitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Int)

    @Insert
    suspend fun insertHabitRecord(record: HabitRecordEntity)

    @Query("""
        SELECT COUNT(*) FROM habit_records 
        WHERE habitId = :habitId 
        AND completionDate BETWEEN :startDate AND :endDate
    """)
    suspend fun getHabitCompletionsForPeriod(
        habitId: Int,
        startDate: String,
        endDate: String
    ): Int

    @Query("SELECT COUNT(*) > 0 FROM habit_records WHERE habitId = :habitId AND completionDate = :date")
    suspend fun isHabitCompletedOnDate(habitId: Int, date: String): Boolean

    // In HabitDao.kt
    @Query("SELECT * FROM habit_records WHERE habitId = :habitId AND completionDate >= :startDate ORDER BY completionDate DESC")
    fun getHabitRecordsForWeekLiveData(habitId: Int, startDate: String): LiveData<List<HabitRecordEntity>>
}
