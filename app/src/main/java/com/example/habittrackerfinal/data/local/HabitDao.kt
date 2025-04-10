package com.example.habittrackerfinal.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface HabitDao {

    // --- Existing ---
    @Query("SELECT * FROM habits")
    fun getAllHabits(): LiveData<List<HabitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Int)

    @Insert
    suspend fun insertHabitRecord(record: HabitRecordEntity)

    @Query("SELECT * FROM habits WHERE id = :id LIMIT 1") // New: Get single habit
    suspend fun getHabitById(id: Int): HabitEntity?

    // --- Modified/New for Counts ---
    @Query("SELECT * FROM habit_records WHERE habitId = :habitId AND completionDate >= :startDate ORDER BY completionDate DESC")
    fun getHabitRecordsForWeekLiveData(habitId: Int, startDate: String): LiveData<List<HabitRecordEntity>> // Keep this as is

    // New: Count completions for a specific day
    @Query("SELECT COUNT(*) FROM habit_records WHERE habitId = :habitId AND completionDate = :date")
    suspend fun getCompletionCountForDate(habitId: Int, date: String): Int

    // New: Count completions for a specific day as LiveData (optional but good for reactivity)
    @Query("SELECT COUNT(*) FROM habit_records WHERE habitId = :habitId AND completionDate = :date")
    fun getCompletionCountForDateLiveData(habitId: Int, date: String): LiveData<Int>

    // New: Delete all records for a specific habit on a specific date
    @Query("DELETE FROM habit_records WHERE habitId = :habitId AND completionDate = :date")
    suspend fun deleteRecordsForDate(habitId: Int, date: String)

    // --- Old queries (kept for reference, remove if unused) ---
    @Deprecated("Use getCompletionCountForDate instead")
    @Query("SELECT COUNT(*) FROM habit_records WHERE habitId = :habitId AND completionDate BETWEEN :startDate AND :endDate")
    suspend fun getHabitCompletionsForPeriod(habitId: Int, startDate: String, endDate: String): Int

    @Deprecated("Use getCompletionCountForDate > 0 instead")
    @Query("SELECT COUNT(*) > 0 FROM habit_records WHERE habitId = :habitId AND completionDate = :date")
    suspend fun isHabitCompletedOnDate(habitId: Int, date: String): Boolean
}