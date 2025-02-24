package com.example.habittrackerfinal.Database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Habits::class], version = 1)
abstract class HabitsDatabase: RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "habits_database"
    }
    abstract fun habitDao(): HabitDao
}