package com.example.habittrackerfinal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String?,
    val completionsPerDay: Int,
    val category: String,
    val icon: String,
    val color: Int,
    val createdDate: Long
)
