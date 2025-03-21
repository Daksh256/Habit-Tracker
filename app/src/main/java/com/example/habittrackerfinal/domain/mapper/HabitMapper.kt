package com.example.habittrackerfinal.domain.mapper

import com.example.habittrackerfinal.data.local.HabitEntity
import com.example.habittrackerfinal.data.model.Habit

fun HabitEntity.toHabit(): Habit {
    return Habit(
        id = id,
        name = name,
        description = description,
        completionsPerDay = completionsPerDay,
        category = category,
        icon = icon,
        color = color,
        createdDate = createdDate // Already Long, no conversion needed
    )
}

fun Habit.toHabitEntity(): HabitEntity {
    return HabitEntity(
        id = id,
        name = name,
        description = description,
        completionsPerDay = completionsPerDay,
        category = category,
        icon = icon,
        color = color,
        createdDate = createdDate // Already Long, no conversion needed
    )
}
