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
    val selectedColor: Int
)