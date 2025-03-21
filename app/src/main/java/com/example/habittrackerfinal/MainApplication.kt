package com.example.habittrackerfinal

import android.app.Application
import androidx.room.Room
import com.example.habittrackerfinal.data.local.HabitDatabase

class MainApplication : Application() {

    companion object {
        // Expose the database as a read-only property.
        lateinit var habitDatabase: HabitDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize the Room database.
        habitDatabase = Room.databaseBuilder(
            applicationContext,
            HabitDatabase::class.java,
            HabitDatabase.DATABASE_NAME
        ).build()
    }
}
