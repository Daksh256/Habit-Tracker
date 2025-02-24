package com.example.habittrackerfinal

import android.app.Application
import androidx.room.Room
import com.example.habittrackerfinal.Database.HabitsDatabase

class MainApplication : Application(){
    companion object{
        lateinit var habitsDatabase: HabitsDatabase
    }

    override fun onCreate() {
        super.onCreate()
        habitsDatabase = Room.databaseBuilder(
            applicationContext,
            HabitsDatabase::class.java,
            HabitsDatabase.DATABASE_NAME
        ).build()

    }
}