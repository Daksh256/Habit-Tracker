package com.example.habittrackerfinal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HabitEntity::class, HabitRecordEntity::class], version = 1)
abstract class HabitDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "habit_database"
    }

    abstract fun habitDao(): HabitDao
}
