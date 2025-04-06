package com.example.habittrackerfinal.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.habittrackerfinal.presentation.viewmodel.HabitViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun StreakTracker(
    viewModel: HabitViewModel,
    habitId: Int,
    habitColor: Color // New parameter: the chosen color of the habit
) {
    val scope = rememberCoroutineScope()
    var streak by remember { mutableStateOf<List<Pair<String, Boolean>>>(emptyList()) }
    val streakData by viewModel.getWeeklyStreakLiveData(habitId).observeAsState(emptyList())

    LaunchedEffect(habitId) {
        scope.launch(Dispatchers.IO) {
            streak = viewModel.getHabitStreakForWeek(habitId)
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp) // Added some padding
    ) {
        // Use the observed streakData directly
        streakData.forEach { (day, completed) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = day)
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        // Use habitColor if completed, otherwise LightGray
                        .background(if (completed) habitColor else Color.LightGray)
                )
            }
        }
    }

}

