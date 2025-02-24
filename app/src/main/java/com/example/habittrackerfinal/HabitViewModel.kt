package com.example.habittrackerfinal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittrackerfinal.Database.Habits
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitViewModel: ViewModel() {

    val habitsDao = MainApplication.habitsDatabase.habitDao()

    val habitsList: LiveData<List<Habits>> = habitsDao.getAll()

    fun addHabit(title: String, description: String?, completionsPerDay: Int, category: String, emoji: String, color: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            habitsDao.addHabits(Habits(name = title, description = description, completionsPerDay = completionsPerDay, selectedCategory = category, selectedIcon = emoji, selectedColor = color))
        }
    }

    fun deleteHabit(id: Int){
        habitsDao.deleteHabit(id)
    }


}