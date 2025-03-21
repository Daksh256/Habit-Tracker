package com.example.habittrackerfinal.data.model

data class Habit(
    val id: Int,
    val name: String,
    val description: String?,
    val completionsPerDay: Int,
    val category: String,
    val icon: String,
    val color: Int,
    val createdDate: Long
)
