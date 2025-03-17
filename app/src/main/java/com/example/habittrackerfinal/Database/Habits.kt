package com.example.habittrackerfinal.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habits(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name: String,
    val description: String?,
    val completionsPerDay: Int,
    val selectedCategory: String,
    val selectedIcon: String,
    val selectedColor: Int,
    val createdDate: String
)
@Entity(tableName = "habit_records")
data class HabitRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val habitId: Int, // Foreign Key linking to Habit
    val completionDate: String, // YYYY-MM-DD format
    val completionTimestamp: Long // For sorting (System.currentTimeMillis())
)
