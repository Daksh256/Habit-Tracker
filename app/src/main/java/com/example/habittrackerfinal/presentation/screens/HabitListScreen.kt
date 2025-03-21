package com.example.habittrackerfinal.presentation.screens


import com.example.habittrackerfinal.presentation.components.HabitItem
import com.example.habittrackerfinal.presentation.components.StreakTracker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import com.example.habittrackerfinal.presentation.viewmodel.HabitViewModel


@Composable
fun HabitListScreen(viewModel: HabitViewModel, onNavigateToAddHabit: () -> Unit) {
    //val habits by viewModel.habitsList.observeAsState(emptyList())
    val habits by viewModel.habitsList.collectAsState()


    Column {
        Button(onClick = onNavigateToAddHabit) {
            Text("Add Habit")
        }
        LazyColumn {
            items(habits) { habit ->
                HabitItem(
                    habit = habit,
                    onDelete = { viewModel.deleteHabit(habit.id) },
                    onComplete = { viewModel.completeHabit(habit.id) },
                    viewModel = viewModel
                )
                StreakTracker(viewModel = viewModel, habitId = habit.id,habitColor = Color(habit.color))
            }
        }
    }
}
