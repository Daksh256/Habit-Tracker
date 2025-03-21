package com.example.habittrackerfinal.domain.usecase


import com.example.habittrackerfinal.data.model.Habit
import com.example.habittrackerfinal.data.repository.HabitRepository

class AddHabitUseCase(private val repository: HabitRepository) {
    suspend operator fun invoke(habit: Habit) {
        // This will call your repository's addHabit function,
        // which converts the Habit model to a HabitEntity and inserts it in the DB.
        repository.addHabit(habit)
    }
}
