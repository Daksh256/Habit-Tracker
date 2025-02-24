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

}